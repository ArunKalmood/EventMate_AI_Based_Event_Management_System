package com.springboard.eventmate.service.recommendation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.springboard.eventmate.ai.ranking.GroqRankingService;
import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.UserActivity;
import com.springboard.eventmate.repository.EventRepository;
import com.springboard.eventmate.repository.UserActivityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RuleBasedRecommendationService {
	
	private final UserActivityRepository userActivityRepository;
    private final EventRepository eventRepository;
    private final GroqRankingService groqRankingService;
    private static final int WEIGHT_SEARCH_MATCH = 5;
    private static final int WEIGHT_VIEW_MATCH = 3;
    private static final int WEIGHT_FILLING_FAST = 4;
    private static final int WEIGHT_TRENDING = 6;
    private static final int PENALTY_VIEWED_EVENT = -8;
    private static final int BONUS_UNSEEN_EVENT = 4;

    public List<Event> getCuratedEvents(Long userId) {

        if (userId == null) return List.of();

        List<UserActivity> activities =
                userActivityRepository.findByUserId(userId);

        if (activities.isEmpty()) {
            List<Event> fallback =
                    eventRepository.findUpcomingByCategories(List.of("MUSIC", "TECH"));

            return groqRankingService.rankEvents(fallback.stream().limit(10).toList());
        }

        // =========================
        // 1️ CATEGORY SCORING
        // =========================
        Map<String, Integer> categoryScore = new HashMap<>();

        for (UserActivity activity : activities) {

            String action = activity.getActionType();

            // SEARCH → category comes from action_value.flit
            if ("SEARCH".equals(action) && activity.getActionValue() != null) {
                categoryScore.merge(
                    activity.getActionValue().toUpperCase(),
                    WEIGHT_SEARCH_MATCH,
                    Integer::sum
                );
            }

            // VIEW_EVENT → derive category from event
            if ("VIEW_EVENT".equals(action) && activity.getEventId() != null) {
                eventRepository.findById(activity.getEventId())
                    .map(Event::getCategory)
                    .filter(Objects::nonNull)
                    .ifPresent(cat ->
                        categoryScore.merge(
                            cat.toUpperCase(),
                            WEIGHT_VIEW_MATCH,
                            Integer::sum
                        )
                    );
            }
        }

        if (categoryScore.isEmpty()) return List.of();

        // =========================
        // 2️ SORT CATEGORIES BY SCORE
        // =========================
        List<String> sortedCategories =
                categoryScore.entrySet()
                        .stream()
                        .sorted((a, b) -> b.getValue() - a.getValue())
                        .map(Map.Entry::getKey)
                        .toList();

        // =========================
        // 3️ FETCH UPCOMING EVENTS
        // =========================
        List<Event> candidates =
                eventRepository.findUpcomingByCategories(sortedCategories);

        if (candidates.isEmpty()) return List.of();
        
	    // =========================
	    // 3️ EVENT SCORE MAP (INIT)
	    // =========================
        
        Map<Long, Integer> eventScore = new HashMap<>();

        for (Event event : candidates) {
            eventScore.put(event.getId(), 0);
        }
        
	    // =========================
	    // 4️ SEARCH → EVENT SCORE
	    // =========================
        
        for (Event event : candidates) {

            String eventCategory = event.getCategory();
            if (eventCategory == null) continue;

            Integer scoreFromCategory =
                    categoryScore.get(eventCategory.toUpperCase());

            if (scoreFromCategory != null) {
                eventScore.merge(
                        event.getId(),
                        scoreFromCategory * WEIGHT_SEARCH_MATCH,
                        Integer::sum
                );
            }
        }
        
	    // =========================
	    // 5️ EVENT STATE BOOST
	    // =========================
        
        for (Event event : candidates) {

            if ("FILLING_FAST".equalsIgnoreCase(event.getStatus().name())) {
                eventScore.merge(
                    event.getId(),
                    WEIGHT_FILLING_FAST,
                    Integer::sum
                );
            }
        }

        // =========================
        // 4️ EXCLUDE VIEWED EVENTS
        // =========================
        Set<Long> viewedEventIds =
                activities.stream()
                        .filter(a -> "VIEW_EVENT".equals(a.getActionType()))
                        .map(UserActivity::getEventId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

        // =========================
        //  STEP 2 — SOFT PENALTY / BONUS (ADDED)
        // =========================
        for (Event event : candidates) {
            if (viewedEventIds.contains(event.getId())) {
                eventScore.merge(
                    event.getId(),
                    PENALTY_VIEWED_EVENT,
                    Integer::sum
                );
            } else {
                eventScore.merge(
                    event.getId(),
                    BONUS_UNSEEN_EVENT,
                    Integer::sum
                );
            }
        }
        
	     // =========================
	     //  STEP 3A — TRENDING BOOST
	     // =========================
        
        Map<Long, Long> viewCounts =
                activities.stream()
                        .filter(a -> "VIEW_EVENT".equals(a.getActionType()))
                        .map(UserActivity::getEventId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.groupingBy(id -> id, Collectors.counting()));

        for (Event event : candidates) {
            long views = viewCounts.getOrDefault(event.getId(), 0L);
            if (views >= 3) {
                eventScore.merge(
                    event.getId(),
                    WEIGHT_TRENDING,
                    Integer::sum
                );
            }
        }
        
	     // =========================
	     //  STEP 3B — URGENCY BOOST
	     // =========================
        
        for (Event event : candidates) {
            if (event.getDate() != null &&
                event.getDate().isBefore(java.time.LocalDate.now().plusDays(3))) {

                eventScore.merge(
                    event.getId(),
                    5,
                    Integer::sum
                );
            }
        }

        // =========================
        // 5️ FINAL SELECTION
        // =========================
        List<Event> ruleRanked =
        	    candidates.stream()
                .sorted((a, b) ->
                    Integer.compare(
                        eventScore.getOrDefault(b.getId(), 0),
                        eventScore.getOrDefault(a.getId(), 0)
                    )
                )
                .limit(10)
                .toList();

        //  FINAL STEP — AI decides the order
        return groqRankingService.rankEvents(ruleRanked);
    }
}
