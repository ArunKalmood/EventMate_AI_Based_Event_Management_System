package com.springboard.eventmate.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.springboard.eventmate.model.Review;
import com.springboard.eventmate.repository.ReviewRepository;
import com.springboard.eventmate.service.ReviewSentimentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewSentimentServiceImpl implements ReviewSentimentService {

    private final ReviewRepository reviewRepository;

    @Override
    @Async
    public void analyzeAsync(Long reviewId, String comment) {

        //  No comment → no sentiment
        if (comment == null || comment.isBlank()) return;

        try {
            //  TEMP LOGIC (REPLACE WITH REAL AI CALL)
            double score = simpleHeuristic(comment);

            Review review = reviewRepository.findById(reviewId).orElse(null);
            if (review == null || Boolean.TRUE.equals(review.getIsDeleted())) return;

            review.setSentimentScore(score);
            reviewRepository.save(review);

        } catch (Exception e) {
            //❗ Swallow exception — never break main flow
        }
    }

    // VERY SIMPLE PLACEHOLDER
    private double simpleHeuristic(String text) {
        String lower = text.toLowerCase();
        if (lower.contains("bad") || lower.contains("worst")) return -0.5;
        if (lower.contains("good") || lower.contains("great") || lower.contains("awesome")) return 0.7;
        return 0.0;
    }
}
