package com.springboard.eventmate.service.impl;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboard.eventmate.ai.ranking.GroqRankingService;
import com.springboard.eventmate.exception.AccessDeniedException;
import com.springboard.eventmate.exception.EventNotFoundException;
import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.Notification;
import com.springboard.eventmate.model.User;
import com.springboard.eventmate.model.dto.CreateEventRequest;
import com.springboard.eventmate.model.dto.EventResponse;
import com.springboard.eventmate.model.dto.EventSummaryResponseDTO;
import com.springboard.eventmate.model.dto.OrganizerEventSummaryDTO;
import com.springboard.eventmate.model.dto.UpdateEventRequest;
import com.springboard.eventmate.model.enums.BookingStatus;
import com.springboard.eventmate.model.enums.EventStatus;
import com.springboard.eventmate.model.enums.NotificationType;
import com.springboard.eventmate.repository.BookingRepository;
import com.springboard.eventmate.repository.EventRepository;
import com.springboard.eventmate.repository.EventReviewStatsRepository;
import com.springboard.eventmate.repository.NotificationRepository;
import com.springboard.eventmate.repository.UserRepository;
import com.springboard.eventmate.service.CloudinaryStorageService;
import com.springboard.eventmate.service.EventService;
import com.springboard.eventmate.service.recommendation.RuleBasedRecommendationService;
import com.springboard.eventmate.smart.rules.DemandAggregationService;
import com.springboard.eventmate.smart.rules.DemandClassificationResult;
import com.springboard.eventmate.smart.rules.DemandNotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final EventReviewStatsRepository eventReviewStatsRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    // EV-389 / EV-391 Smart Rules
    private final DemandAggregationService demandAggregationService;
    private final DemandNotificationService demandNotificationService;
    
    //EV-403
    private final NotificationRepository notificationRepository;
    
    private final CloudinaryStorageService cloudinaryStorageService;
    
    @Autowired
    private GroqRankingService groqRankingService;

    @Autowired
    private RuleBasedRecommendationService ruleBasedRecommendationService;




    // =========================
    // CREATE EVENT
    // =========================
    @Override
    public Event createEvent(CreateEventRequest request, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setDate(request.getDate());
        event.setTime(request.getTime());
        event.setLocation(request.getLocation());
        event.setCapacity(request.getCapacity());
        event.setPrice(request.getPrice());
        event.setCreatedBy(user);
        event.setCategory(request.getNormalizedCategory());
        event.setTags(normalizeAndValidateTags(request.getTags()));

        // 1️ Save first to get eventId
        Event savedEvent = eventRepository.save(event);

        // 2️ Upload banner image (if provided)
        MultipartFile bannerImage = request.getBannerImage();
        if (bannerImage != null && !bannerImage.isEmpty()) {

            String bannerUrl =
                    cloudinaryStorageService.uploadEventBanner(
                            bannerImage,
                            savedEvent.getId()
                    );

            savedEvent.setBannerImageUrl(bannerUrl);

            // 3️ Persist banner URL
            savedEvent = eventRepository.save(savedEvent);
        }

        // 4️ Clear cache
        try {
            redisTemplate.delete("events:all");
        } catch (Exception e) {
            // ignore cache failure
        }

        return savedEvent;
    }


    // =========================
    // GET ALL EVENTS (CACHED)
    // =========================
    @Override
    public List<EventResponse> getAllEvents(String userEmail) {

        String cacheKey = "events:all";

        try {
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                return (List<EventResponse>) cached;
            }
        } catch (Exception e) {
            // ignore redis failure
        }

        User user = resolveUser(userEmail);

        List<EventResponse> events = eventRepository.findAll()
                .stream()
                .map(event -> mapToEventResponse(event, user))
                .collect(Collectors.toList());

        try {
            redisTemplate.opsForValue()
                    .set(cacheKey, events, Duration.ofMinutes(5));
        } catch (Exception e) {
            // ignore cache failure
        }

        return events;
    }

    // =========================
    // GET EVENT BY ID (CACHED)
    // =========================
    @Override
    public EventResponse getEventById(Long id, String userEmail) {

        String cacheKey = "event:" + id;

        try {
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                return (EventResponse) cached;
            }
        } catch (Exception e) {
            // ignore redis failure
        }

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        User user = resolveUser(userEmail);
        EventResponse response = mapToEventResponse(event, user);

        try {
            redisTemplate.opsForValue()
                    .set(cacheKey, response, Duration.ofMinutes(5));
        } catch (Exception e) {
            // ignore cache failure
        }

        return response;
    }

    // =========================
    // DELETE EVENT
    // =========================
    @Override
    public void deleteEvent(Long eventId, String userEmail) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!event.getCreatedBy().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to delete this event");
        }

        eventRepository.delete(event);

        try {
            redisTemplate.delete("events:all");
            redisTemplate.delete("event:" + eventId);
        } catch (Exception e) {
            // ignore cache failure
        }
    }

    // =========================
    // CLOSE EVENT
    // =========================
    @Override
    public void closeEvent(Long eventId, String organizerEmail) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        if (!event.getCreatedBy().getEmail().equals(organizerEmail)) {
            throw new AccessDeniedException("You are not allowed to close this event");
        }

        if (event.getStatus() == EventStatus.CLOSED) return;

        event.setStatus(EventStatus.CLOSED);
        eventRepository.save(event);

        try {
            redisTemplate.delete("events:all");
            redisTemplate.delete("event:" + eventId);
        } catch (Exception e) {
            // ignore cache failure
        }
    }

    // =========================
    // SEARCH EVENTS (NO CACHE)
    // =========================
    @Override
    public Page<EventSummaryResponseDTO> searchEvents(
            String keyword,
            String category,
            int page,
            int size,
            String userEmail) {

        PageRequest pageable = PageRequest.of(page, size);
        User user = resolveUser(userEmail);

        String normalizedCategory = category;


        Page<Event> events =
                (normalizedCategory != null)
                        ? eventRepository.searchByTitleOrLocationAndCategory(
                                keyword == null ? "" : keyword,
                                normalizedCategory,
                                pageable
                          )
                        : eventRepository.searchByTitleOrLocation(
                                keyword == null ? "" : keyword,
                                pageable
                          );


        return events.map(event -> mapToEventSummary(event, user));
    }

    // =========================
    // ORGANIZER EVENT SUMMARY
    // =========================
    @Override
    public List<EventSummaryResponseDTO> getOrganizerEventSummary(String organizerEmail) {

        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Event> events = eventRepository.findByCreatedBy(organizer);

        return events.stream()
                .map(event -> mapToEventSummary(event, organizer))
                .collect(Collectors.toList());
    }

    // =========================
    // MAPPERS
    // =========================
    private EventResponse mapToEventResponse(Event event, User user) {

        long bookedCount =
                bookingRepository.countByEventAndStatus(event, BookingStatus.CONFIRMED);

        int capacity = event.getCapacity() == null ? 0 : event.getCapacity();
        int availableSeats = Math.max(0, capacity - (int) bookedCount);

        boolean bookedByUser =
                user != null && bookingRepository.existsByUserAndEventAndStatus(
                        user, event, BookingStatus.CONFIRMED);

        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setDate(event.getDate());
        response.setTime(event.getTime());
        response.setLocation(event.getLocation());
        response.setOrganizerName(event.getCreatedBy().getName());
        response.setOrganizerEmail(event.getCreatedBy().getEmail());
        response.setCapacity(capacity);
        response.setBookedCount(bookedCount);
        response.setAvailableSeats(availableSeats);
        response.setPrice(event.getPrice());
        response.setStatus(event.getStatus().name());
        response.setCategory(event.getCategory());
        response.setTags(event.getTags());
        response.setBookedByUser(bookedByUser);
        response.setBannerImageUrl(event.getBannerImageUrl());

        // -------- EV-389 + EV-391 --------
        DemandClassificationResult demandResult =
                demandAggregationService.evaluate(event);

        if (demandResult != null) {

            // EV-391: emit notification on state transition
            demandNotificationService.processDemandTransition(event, demandResult);

            response.setDemandState(demandResult.getDemandState().name());
            response.setCapacityUsagePercentage(
                    demandResult.getDemandMetrics()
                            .getCapacityUtilizationPercentage()
            );
            response.setOvercrowdingRisk(
                    demandResult.isOvercrowdingRisk()
            );
        }
        
        // -------- REVIEW STATS --------
        
        eventReviewStatsRepository.findById(event.getId())
        .ifPresent(stats -> {
            response.setAvgRating(stats.getAvgRating());
            response.setReviewCount(stats.getReviewCount());
            response.setIsQualified(stats.getIsQualified());
        });

        return response;
    }

    private EventSummaryResponseDTO mapToEventSummary(Event event, User user) {

        long bookedCount =
                bookingRepository.countByEventAndStatus(event, BookingStatus.CONFIRMED);

        int capacity = event.getCapacity() == null ? 0 : event.getCapacity();
        int availableSeats = Math.max(0, capacity - (int) bookedCount);

        String status =
                capacity == 0 ? "NO_CAPACITY"
                        : bookedCount >= capacity ? "FULL"
                        : bookedCount >= capacity * 0.7 ? "FILLING_FAST"
                        : "AVAILABLE";

        EventSummaryResponseDTO dto = new EventSummaryResponseDTO();
        dto.setEventId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDate(event.getDate());
        dto.setTime(event.getTime());
        dto.setCapacity(capacity);
        dto.setBookedCount(bookedCount);
        dto.setAvailableSeats(availableSeats);
        dto.setPrice(event.getPrice());
        dto.setStatus(status);
        dto.setCategory(event.getCategory());
        dto.setTags(event.getTags());
        dto.setBannerImageUrl(event.getBannerImageUrl());

        // -------- EV-389 + EV-391 --------
        DemandClassificationResult demandResult =
                demandAggregationService.evaluate(event);

        if (demandResult != null) {

            // EV-391: emit notification on state transition
            demandNotificationService.processDemandTransition(event, demandResult);

            dto.setDemandState(demandResult.getDemandState().name());
            dto.setCapacityUsagePercentage(
                    demandResult.getDemandMetrics()
                            .getCapacityUtilizationPercentage()
            );
            dto.setOvercrowdingRisk(
                    demandResult.isOvercrowdingRisk()
            );
        }
        
        // -------- REVIEW STATS --------
        
        eventReviewStatsRepository.findById(event.getId())
        .ifPresent(stats -> {
            dto.setAvgRating(stats.getAvgRating());
            dto.setIsQualified(stats.getIsQualified());
        });

        return dto;
    }

    private User resolveUser(String email) {
        if (email == null) return null;
        return userRepository.findByEmail(email).orElse(null);
    }

    private List<String> normalizeAndValidateTags(List<String> tags) {

        if (tags == null) return null;

        List<String> normalized = tags.stream()
                .map(String::trim)
                .collect(Collectors.toList());

        long distinctCount = normalized.stream()
                .map(String::toLowerCase)
                .distinct()
                .count();

        if (distinctCount != normalized.size()) {
            throw new IllegalArgumentException("Duplicate tags are not allowed");
        }

        return normalized;
    }

    @Override
    public Event updateEvent(Long eventId, UpdateEventRequest request, String userEmail) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!event.getCreatedBy().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to update this event");
        }

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setDate(request.getDate());
        event.setTime(request.getTime());
        event.setLocation(request.getLocation());
        event.setCapacity(request.getCapacity());
        event.setPrice(request.getPrice());
        event.setCategory(request.getNormalizedCategory());
        event.setTags(normalizeAndValidateTags(request.getTags()));

        Event updated = eventRepository.save(event);
        
     // EV-403 — Event Update Notification
        List<User> bookedUsers =
                bookingRepository.findByEventAndStatus(event, BookingStatus.CONFIRMED)
                                 .stream()
                                 .map(b -> b.getUser())
                                 .distinct()
                                 .collect(Collectors.toList());

        for (User bookedUser : bookedUsers) {

            Notification notification = new Notification();
            notification.setUser(bookedUser);
            notification.setType(NotificationType.EVENT);
            notification.setTitle("Event Updated");
            notification.setMessage(
                    "The event \"" + event.getTitle() + "\" has been updated. Please review the details."
            );
            notification.setEventId(event.getId());
            notification.setRelatedEntityId(event.getId());
            notification.setRead(false);

            notificationRepository.save(notification);
        }


        try {
            redisTemplate.delete("events:all");
            redisTemplate.delete("event:" + eventId);
        } catch (Exception e) {
            // ignore cache failure
        }

        return updated;
    }
    
    private OrganizerEventSummaryDTO mapToOrganizerSummary(Event event) {

        long bookedCount =
                bookingRepository.countByEventAndStatus(
                        event, BookingStatus.CONFIRMED);

        int capacity = event.getCapacity() == null ? 0 : event.getCapacity();
        int remainingSeats = Math.max(0, capacity - (int) bookedCount);

        int usagePercentage =
                capacity == 0 ? 0 : (int) ((bookedCount * 100) / capacity);

        String status;
        if (event.getStatus() == EventStatus.CLOSED) {
            status = "CLOSED";
        } else if (bookedCount >= capacity && capacity > 0) {
            status = "FULL";
        } else {
            status = "ACTIVE";
        }

        OrganizerEventSummaryDTO dto = new OrganizerEventSummaryDTO();
        dto.setEventId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDate(event.getDate());
        dto.setTime(event.getTime());
        dto.setLocation(event.getLocation());

        dto.setCapacity(capacity);
        dto.setBookedCount(bookedCount);
        dto.setRemainingSeats(remainingSeats);
        dto.setCapacityUsagePercentage(usagePercentage); //  EV-430
        dto.setStatus(status);

        // ===============================
        // EV-435 — Smart Rules Exposure
        // ===============================
        DemandClassificationResult demandResult =
                demandAggregationService.evaluate(event);

        if (demandResult != null) {
            dto.setDemandState(demandResult.getDemandState().name());
            dto.setOvercrowdingRisk(demandResult.isOvercrowdingRisk());
        }

        return dto;
    }



    
    @Override
    public List<OrganizerEventSummaryDTO> getOrganizerEventSummaries(String organizerEmail) {

        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return eventRepository.findByCreatedBy(organizer)
                .stream()
                .map(this::mapToOrganizerSummary)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<OrganizerEventSummaryDTO> getOrganizerEventSummaries(
            String organizerEmail,
            int page,
            int size) {

        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PageRequest pageable = PageRequest.of(page, size);

        return eventRepository.findByCreatedBy(organizer, pageable)
                .map(this::mapToOrganizerSummary);
    }
    
	 // =========================
	 // PUBLIC MAPPER (REUSE)
	 // =========================
    
    public EventResponse toEventResponse(Event event, String userEmail) {
        User user = resolveUser(userEmail);
        return mapToEventResponse(event, user);
    }
    
    @Override
    public List<EventResponse> getCuratedEvents(String userEmail) {

        User user = resolveUser(userEmail);
        if (user == null) return List.of();

        // 1️ Rule-based curated events
        List<Event> curatedEvents =
                ruleBasedRecommendationService.getCuratedEvents(user.getId());

        if (curatedEvents.isEmpty()) return List.of();

        // 2️ Extract user signals (simple, safe)
        List<String> userSignals = List.of(
                "recent searches",
                "viewed events",
                "preferred categories"
        );

        // 3️ AI explanations
        Map<Long, String> aiReasons =
                groqRankingService.explainRecommendations(
                        curatedEvents,
                        userSignals
                );

        // 4️ Map → DTO + attach explanation
        return curatedEvents.stream()
                .map(event -> {
                    EventResponse response = mapToEventResponse(event, user);
                    response.setRecommendationReason(
                            aiReasons.get(event.getId())
                    );
                    return response;
                })
                .collect(Collectors.toList());	
    }

    

}
