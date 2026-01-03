package com.springboard.eventmate.model.dto;

import java.time.LocalDateTime;

public class LostItemResponse {

    private Long id;
    private Long eventId;          // ADDED
    private String title;
    private String description;
    private String imageUrl;       // Sprint-4
    private String status;
    private LocalDateTime createdAt;

    public LostItemResponse(
            Long id,
            Long eventId,          // ADDED
            String title,
            String description,
            String imageUrl,
            String status,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.eventId = eventId;    // ADDED
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
    }

    // getters

    public Long getId() {
        return id;
    }

    public Long getEventId() {     // ADDED
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
