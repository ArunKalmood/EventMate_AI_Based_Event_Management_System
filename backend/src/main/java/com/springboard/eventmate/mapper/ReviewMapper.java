package com.springboard.eventmate.mapper;

import com.springboard.eventmate.model.Review;
import com.springboard.eventmate.model.dto.ReviewResponseDTO;

public class ReviewMapper {

    public static ReviewResponseDTO toDTO(Review review) {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(review.getId());
        dto.setEventId(review.getEvent().getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setSentimentScore(review.getSentimentScore());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setEventTitle(review.getEvent().getTitle());
        return dto;
    }
}
