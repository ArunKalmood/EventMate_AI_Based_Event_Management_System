package com.springboard.eventmate.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.EventReviewStats;
import com.springboard.eventmate.repository.EventRepository;
import com.springboard.eventmate.repository.EventReviewStatsRepository;
import com.springboard.eventmate.repository.ReviewRepository;
import com.springboard.eventmate.service.EventReviewStatsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventReviewStatsServiceImpl
        implements EventReviewStatsService {

    private final ReviewRepository reviewRepository;
    private final EventReviewStatsRepository statsRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public void recompute(Long eventId) {

        Object[] raw = reviewRepository.calculateStats(eventId);
        Object[] result = (raw != null && raw.length > 0 && raw[0] instanceof Object[])
                ? (Object[]) raw[0]
                : raw;

        double avgRating = 0.0;
        int reviewCount = 0;
        double positiveRatio = 0.0;

        if (result != null) {
            if (result[0] != null) {
                avgRating = ((Number) result[0]).doubleValue();
            }
            if (result[1] != null) {
                reviewCount = ((Number) result[1]).intValue();
            }
            if (result[2] != null) {
                positiveRatio = ((Number) result[2]).doubleValue();
            }
        }

        EventReviewStats stats = statsRepository
            .findById(eventId)
            .orElseGet(() -> {
                Event event = eventRepository.findById(eventId)
                    .orElseThrow(() ->
                        new RuntimeException("Event not found for stats recompute")
                    );
                EventReviewStats s = new EventReviewStats();
                s.setEvent(event);
                return s;
            });

        stats.setAvgRating(avgRating);
        stats.setReviewCount(reviewCount);
        stats.setPositiveRatio(positiveRatio);

        // =========================
        // QUALIFIED RULE (LOCKED)
        // =========================
        stats.setIsQualified(
            reviewCount >= 5 &&
            avgRating >= 4.0 &&
            positiveRatio >= 0.7
        );

        statsRepository.save(stats);
    }
}
