package com.springboard.eventmate.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboard.eventmate.model.Review;

public interface ReviewService {

    Review createReview(Long eventId, Integer rating, String comment);

    Review updateReview(Long reviewId, Integer rating, String comment);

    void deleteReview(Long reviewId);

    Page<Review> getEventReviews(Long eventId, Pageable pageable);

    Page<Review> getLatestReviews(Pageable pageable);
}
