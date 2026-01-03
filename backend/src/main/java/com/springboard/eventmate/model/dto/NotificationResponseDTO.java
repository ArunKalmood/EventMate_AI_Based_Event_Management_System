package com.springboard.eventmate.model.dto;

import java.time.LocalDateTime;

import com.springboard.eventmate.model.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationResponseDTO {

    private Long notificationId;
    private NotificationType type;
    private String message;
    private Long relatedEntityId;
    private boolean isRead;
    private LocalDateTime createdAt;
}
