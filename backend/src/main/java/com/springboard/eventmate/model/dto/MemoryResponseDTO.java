package com.springboard.eventmate.model.dto;

import java.time.LocalDateTime;

import com.springboard.eventmate.model.enums.MediaType;

public class MemoryResponseDTO {

    private Long memoryId;
    private String mediaUrl;
    private MediaType mediaType;
    private LocalDateTime createdAt;
    private Long eventId;

    public MemoryResponseDTO(
    		Long memoryId,
            String mediaUrl,
            MediaType mediaType,
            Long eventId,
            LocalDateTime createdAt
    ) 
    {
		this.memoryId = memoryId;
		this.mediaUrl = mediaUrl;
		this.mediaType = mediaType;
		this.eventId = eventId;
		this.createdAt = createdAt;
	}
    
    public Long getMemoryId() {
		return memoryId;
	}
    
    public String getMediaUrl() {
		return mediaUrl;
	}
    
    public MediaType getMediaType() {
		return mediaType;
	}
	
    public Long getEventId() {
		return eventId;
	}
	
    public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
