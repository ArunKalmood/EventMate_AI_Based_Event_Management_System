package com.springboard.eventmate.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.Review;
import com.springboard.eventmate.model.User;
import com.springboard.eventmate.model.enums.BookingStatus;
import com.springboard.eventmate.repository.BookingRepository;
import com.springboard.eventmate.repository.EventRepository;
import com.springboard.eventmate.repository.ReviewRepository;
import com.springboard.eventmate.repository.UserRepository;
import com.springboard.eventmate.service.EventReviewStatsService;
import com.springboard.eventmate.service.ReviewSentimentService;
import com.springboard.eventmate.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;
    private final EventReviewStatsService eventReviewStatsService;
    private final ReviewSentimentService reviewSentimentService;
    private final UserRepository userRepository;

    // =========================
    // CREATE REVIEW
    // =========================
    @Override
    @Transactional
    public Review createReview(Long eventId, Integer rating, String comment) {

        Object principal = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        String email;

        if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
            email = springUser.getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        } else {
            throw new RuntimeException("Invalid authentication principal");
        }

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // 1️ Check booking exists (must have attended)
        bookingRepository
            .findByUserIdAndEventIdAndStatus(
                user.getId(),
                eventId,
                BookingStatus.CONFIRMED
            )
            .orElseThrow(() ->
                new RuntimeException("You must attend the event before reviewing")
            );

        // 2️ Prevent duplicate review
        reviewRepository
            .findByEventIdAndUserIdAndIsDeletedFalse(eventId, user.getId())
            .ifPresent(r -> {
                throw new RuntimeException("You already reviewed this event");
            });

        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));

        Review review = new Review();
        review.setEvent(event);
        review.setUser(user);
        review.setRating(rating);
        review.setComment(comment);

        Review saved = reviewRepository.save(review);

        //  Async sentiment analysis
        reviewSentimentService.analyzeAsync(saved.getId(), saved.getComment());

        //  Recompute aggregates
        eventReviewStatsService.recompute(eventId);

        return saved;
    }

    // =========================
    // UPDATE REVIEW
    // =========================
    @Override
    @Transactional
    public Review updateReview(Long reviewId, Integer rating, String comment) {

        Object principal = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        String email;

        if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
            email = springUser.getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        } else {
            throw new RuntimeException("Invalid authentication principal");
        }

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("Review not found"));

        // 🔐 Ownership check
        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can update only your own review");
        }

        review.setRating(rating);
        review.setComment(comment);

        Review saved = reviewRepository.save(review);

        //  re-analyze sentiment
        reviewSentimentService.analyzeAsync(saved.getId(), saved.getComment());

        //  Recompute aggregates
        eventReviewStatsService.recompute(review.getEvent().getId());

        return saved;
    }

    // =========================
    // DELETE REVIEW (SOFT)
    // =========================
    @Override
    @Transactional
    public void deleteReview(Long reviewId) {

        Object principal = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        String email;

        if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
            email = springUser.getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        } else {
            throw new RuntimeException("Invalid authentication principal");
        }

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("Review not found"));

        //  Ownership check
        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can delete only your own review");
        }

        review.setIsDeleted(true);
        reviewRepository.save(review);

        //  Recompute aggregates
        eventReviewStatsService.recompute(review.getEvent().getId());
    }

    // =========================
    // READ EVENT REVIEWS
    // =========================
    @Override
    public Page<Review> getEventReviews(Long eventId, Pageable pageable) {
        return reviewRepository
            .findByEventIdAndIsDeletedFalseOrderByCreatedAtDesc(eventId, pageable);
    }

    // =========================
    // READ LATEST REVIEWS (GLOBAL)
    // =========================
    @Override
    @Transactional(readOnly = true)
    public Page<Review> getLatestReviews(Pageable pageable) {
        return reviewRepository
            .findLatestWithEventAndUser(pageable);
    }

}
