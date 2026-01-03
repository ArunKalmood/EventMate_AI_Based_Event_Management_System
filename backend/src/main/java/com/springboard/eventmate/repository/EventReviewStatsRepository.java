package com.springboard.eventmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboard.eventmate.model.EventReviewStats;

public interface EventReviewStatsRepository
        extends JpaRepository<EventReviewStats, Long> {
}
