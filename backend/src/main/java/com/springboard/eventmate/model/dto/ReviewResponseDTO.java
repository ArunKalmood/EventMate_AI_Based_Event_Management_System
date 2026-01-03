package com.springboard.eventmate.model.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewResponseDTO {

    private Long id;
    private Long eventId;
    private Long userId;
    private String userName;

    private Integer rating;
    private String comment;

    private Double sentimentScore;

    private LocalDateTime createdAt;
    
    private String eventTitle;
}
