package com.springboard.eventmate.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.springboard.eventmate.exception.EventNotFoundException;
import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.LostItem;
import com.springboard.eventmate.model.Notification;
import com.springboard.eventmate.model.User;
import com.springboard.eventmate.model.dto.LostAndFoundSummaryDTO;
import com.springboard.eventmate.model.dto.LostItemResponse;
import com.springboard.eventmate.model.enums.LostItemStatus;
import com.springboard.eventmate.repository.EventRepository;
import com.springboard.eventmate.repository.LostItemRepository;
import com.springboard.eventmate.repository.NotificationRepository;
import com.springboard.eventmate.repository.UserRepository;
import com.springboard.eventmate.service.LostItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LostItemServiceImpl implements LostItemService {

    private final LostItemRepository lostItemRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    // =========================
    // REPORT LOST ITEM (USER)
    // =========================
    @Override
    public LostItemResponse reportLostItem(Long eventId, String title, String description) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LostItem item = new LostItem();
        item.setEvent(event);
        item.setReportedBy(user);
        item.setTitle(title);
        item.setDescription(description);
        item.setStatus(LostItemStatus.PENDING);
        item.setImageUrl(null);

        LostItem saved = lostItemRepository.save(item);

        return new LostItemResponse(
                saved.getId(),
                saved.getEvent().getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getImageUrl(),
                saved.getStatus().name(),
                saved.getCreatedAt()
        );
    }

    // =========================
    // GET LOST ITEMS BY EVENT
    // =========================
    @Override
    public List<LostItemResponse> getLostItemsByEvent(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        return lostItemRepository
                .findByEventIdOrderByCreatedAtDesc(event.getId())
                .stream()
                .map(item -> new LostItemResponse(
                        item.getId(),
                        item.getEvent().getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getImageUrl(),
                        item.getStatus().name(),
                        item.getCreatedAt()
                ))
                .toList();
    }

    // =========================
    // ORGANIZER VIEW
    // =========================
    @Override
    public List<LostItemResponse> getLostItemsForOrganizer() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User organizer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return lostItemRepository
                .findByEventCreatedByIdOrderByCreatedAtDesc(organizer.getId())
                .stream()
                .map(item -> new LostItemResponse(
                        item.getId(),
                        item.getEvent().getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getImageUrl(),
                        item.getStatus().name(),
                        item.getCreatedAt()
                ))
                .toList();
    }

    // =========================
    // UPDATE LOST ITEM STATUS
    // =========================
    @Override
    public LostItemResponse updateLostItemStatus(Long lostItemId, LostItemStatus status) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User organizer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LostItem item = lostItemRepository.findById(lostItemId)
                .orElseThrow(() -> new RuntimeException("Lost item not found"));

        // Ownership check
        if (!item.getEvent().getCreatedBy().getId().equals(organizer.getId())) {
            throw new RuntimeException("You are not allowed to update this lost item");
        }

        LostItemStatus oldStatus = item.getStatus();

        // Invalid transition → silent ignore
        if (!isValidTransition(oldStatus, status)) {
            return new LostItemResponse(
                    item.getId(),
                    item.getEvent().getId(),
                    item.getTitle(),
                    item.getDescription(),
                    item.getImageUrl(),
                    item.getStatus().name(),
                    item.getCreatedAt()
            );
        }

        item.setStatus(status);
        item.setUpdatedAt(LocalDateTime.now());

        // =========================
        // EV-404 + EV-407 — Safe Notification
        // =========================
        boolean shouldNotify =
                (oldStatus == LostItemStatus.PENDING && status == LostItemStatus.FOUND)
             || (oldStatus == LostItemStatus.FOUND && status == LostItemStatus.RETURNED);

        if (shouldNotify) {

            String title =
                    status == LostItemStatus.FOUND
                            ? "Lost Item Found"
                            : "Lost Item Returned";

            boolean alreadyNotified =
                    notificationRepository.existsByUserIdAndLostItemIdAndTitle(
                            item.getReportedBy().getId(),
                            item.getId(),
                            title
                    );

            if (!alreadyNotified) {
                Notification notification = new Notification();
                notification.setUser(item.getReportedBy());
                notification.setEventId(item.getEvent().getId());
                notification.setLostItemId(item.getId());
                notification.setTitle(title);
                notification.setMessage(
                        "Your lost item \"" + item.getTitle()
                        + "\" status changed to " + status.name()
                );

                try {
                    notificationRepository.save(notification);
                } catch (Exception e) {
                    log.warn(
                        "LostItem notification failed for lostItemId={}",
                        item.getId(),
                        e
                    );
                }
            }
        }

        LostItem saved = lostItemRepository.save(item);

        return new LostItemResponse(
                saved.getId(),
                saved.getEvent().getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getImageUrl(),
                saved.getStatus().name(),
                saved.getCreatedAt()
        );
    }

    // =========================
    // STATUS TRANSITION RULES
    // =========================
    private boolean isValidTransition(LostItemStatus current, LostItemStatus next) {

        if (current == next) return false;

        switch (current) {
            case PENDING:
                return next == LostItemStatus.FOUND;
            case FOUND:
                return next == LostItemStatus.RETURNED;
            case RETURNED:
                return false;
            default:
                return false;
        }
    }
    
    @Override
    public Map<Long, LostAndFoundSummaryDTO> getLostAndFoundSummaryForOrganizer() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User organizer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<LostItem> items =
                lostItemRepository.findByEventCreatedByIdOrderByCreatedAtDesc(
                        organizer.getId()
                );

        Map<Long, LostAndFoundSummaryDTO> summaryMap = new HashMap<>();

        for (LostItem item : items) {

            Long eventId = item.getEvent().getId();

            LostAndFoundSummaryDTO dto =
                    summaryMap.computeIfAbsent(eventId, id -> {
                        LostAndFoundSummaryDTO s = new LostAndFoundSummaryDTO();
                        s.setEventId(id);
                        s.setTotalLostItems(0);
                        s.setPendingCount(0);
                        s.setFoundCount(0);
                        s.setReturnedCount(0);
                        return s;
                    });

            dto.setTotalLostItems(dto.getTotalLostItems() + 1);

            switch (item.getStatus()) {
                case PENDING -> dto.setPendingCount(dto.getPendingCount() + 1);
                case FOUND -> dto.setFoundCount(dto.getFoundCount() + 1);
                case RETURNED -> dto.setReturnedCount(dto.getReturnedCount() + 1);
            }
        }

        return summaryMap;
    }
    
    @Override
    public List<LostItemResponse> getLostItemsForOrganizerEvent(
            Long eventId,
            String organizerEmail
    ) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔒 OWNERSHIP CHECK
        if (!event.getCreatedBy().getId().equals(organizer.getId())) {
            throw new RuntimeException("You are not allowed to view lost items for this event");
        }

        return lostItemRepository
                .findByEventIdOrderByCreatedAtDesc(eventId)
                .stream()
                .map(item -> new LostItemResponse(
                        item.getId(),
                        item.getEvent().getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getImageUrl(),
                        item.getStatus().name(),
                        item.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public List<LostItemResponse> getLostItemsForCurrentUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return lostItemRepository
                .findByReportedBy(user)
                .stream()
                .map(item -> new LostItemResponse(
                        item.getId(),
                        item.getEvent().getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getImageUrl(),
                        item.getStatus().name(),
                        item.getCreatedAt()
                ))
                .toList();
    }



    
}
