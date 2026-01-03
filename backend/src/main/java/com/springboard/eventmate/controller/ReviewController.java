package com.springboard.eventmate.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboard.eventmate.mapper.ReviewMapper;
import com.springboard.eventmate.model.dto.ReviewResponseDTO;
import com.springboard.eventmate.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // =========================
    // CREATE REVIEW
    // =========================
    @PostMapping("/events/{eventId}")
    public ResponseEntity<ReviewResponseDTO> create(
        @PathVariable Long eventId,
        @RequestParam Integer rating,
        @RequestParam(required = false) String comment
    ) {
        return ResponseEntity.ok(
            ReviewMapper.toDTO(
                reviewService.createReview(eventId, rating, comment)
            )
        );
    }

    // =========================
    // READ EVENT REVIEWS
    // (preview / modal)
    // =========================
    @GetMapping("/events/{eventId}")
    public ResponseEntity<?> getEventReviews(
        @PathVariable Long eventId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(
            reviewService
                .getEventReviews(eventId, PageRequest.of(page, size))
                .map(ReviewMapper::toDTO)
        );
    }

    // =========================
    // READ LATEST REVIEWS
    // (landing page – global)
    // =========================
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestReviews() {
        return ResponseEntity.ok(
            reviewService
                .getLatestReviews(PageRequest.of(0, 5))
                .map(ReviewMapper::toDTO)
        );
    }

    // =========================
    // UPDATE REVIEW
    // =========================
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> update(
        @PathVariable Long reviewId,
        @RequestParam Integer rating,
        @RequestParam(required = false) String comment
    ) {
        return ResponseEntity.ok(
            ReviewMapper.toDTO(
                reviewService.updateReview(reviewId, rating, comment)
            )
        );
    }

    // =========================
    // DELETE REVIEW (SOFT)
    // =========================
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
