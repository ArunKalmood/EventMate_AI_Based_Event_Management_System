package com.springboard.eventmate.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.springboard.eventmate.model.Notification;
import com.springboard.eventmate.model.dto.NotificationResponseDTO;

public interface NotificationService {

    Notification save(Notification notification);

    List<NotificationResponseDTO> getMyNotifications();

    List<NotificationResponseDTO> getMyUnreadNotifications();

    Page<NotificationResponseDTO> getMyNotifications(int page, int size);

    Page<NotificationResponseDTO> getMyUnreadNotifications(int page, int size);
    
    void markNotificationAsRead(Long notificationId);
    
    void markAllAsRead();

}
