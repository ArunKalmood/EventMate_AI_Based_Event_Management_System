package com.springboard.eventmate.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.springboard.eventmate.model.Notification;
import com.springboard.eventmate.model.Role;
import com.springboard.eventmate.model.User;
import com.springboard.eventmate.model.dto.NotificationResponseDTO;
import com.springboard.eventmate.model.enums.NotificationType;
import com.springboard.eventmate.repository.NotificationRepository;
import com.springboard.eventmate.repository.UserRepository;
import com.springboard.eventmate.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log =
            LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // =========================
    // SAVE NOTIFICATION (EV-407 SAFE)
    // =========================
    @Override
    public Notification save(Notification notification) {

        if (notification == null) {
            throw new IllegalArgumentException("Notification must not be null");
        }
        if (notification.getUser() == null) {
            throw new IllegalArgumentException("Notification user must not be null");
        }
        if (notification.getType() == null) {
            throw new IllegalArgumentException("Notification type must not be null");
        }
        if (notification.getMessage() == null || notification.getMessage().isBlank()) {
            throw new IllegalArgumentException("Notification message must not be empty");
        }

        try {
            return notificationRepository.save(notification);
        } catch (Exception e) {
            log.warn(
                "Notification save failed [userId={}, type={}, title={}]",
                notification.getUser().getId(),
                notification.getType(),
                notification.getTitle(),
                e
            );
            return null; // EV-407: never break caller flow
        }
    }

    // =========================
    // EV-409 + EV-411 + EV-412 + EV-413
    // =========================
    @Override
    public Page<NotificationResponseDTO> getMyNotifications(int page, int size) {

        User user = getLoggedInUser();
        Set<NotificationType> allowedTypes = allowedTypesForRole(user.getRole());
        Pageable pageable = safePageable(page, size);

        Page<Notification> result =
                notificationRepository.findByUserIdOrderByCreatedAtDesc(
                        user.getId(), pageable
                );

        List<NotificationResponseDTO> mapped =
                result.getContent().stream()
                        .filter(n -> allowedTypes.contains(n.getType()))
                        .map(this::toDTO)
                        .collect(Collectors.toList());

        return new PageImpl<>(mapped, pageable, result.getTotalElements());
    }

    @Override
    public Page<NotificationResponseDTO> getMyUnreadNotifications(int page, int size) {

        User user = getLoggedInUser();
        Set<NotificationType> allowedTypes = allowedTypesForRole(user.getRole());
        Pageable pageable = safePageable(page, size);

        Page<Notification> result =
                notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(
                        user.getId(), pageable
                );

        List<NotificationResponseDTO> mapped =
                result.getContent().stream()
                        .filter(n -> allowedTypes.contains(n.getType()))
                        .map(this::toDTO)
                        .collect(Collectors.toList());

        return new PageImpl<>(mapped, pageable, result.getTotalElements());
    }

    // =========================
    // Non-paginated helpers
    // =========================
    @Override
    public List<NotificationResponseDTO> getMyNotifications() {
        return getMyNotifications(0, 10).getContent();
    }

    @Override
    public List<NotificationResponseDTO> getMyUnreadNotifications() {
        return getMyUnreadNotifications(0, 10).getContent();
    }

    // =========================
    // EV-416 — Mark SINGLE notification as read
    // EV-418 — Ownership & authorization enforced
    // =========================
    @Override
    public void markNotificationAsRead(Long notificationId) {

        User user = getLoggedInUser();

        Notification notification =
                notificationRepository.findByIdAndUserId(notificationId, user.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Notification not found or access denied")
                        );

        // Idempotent operation
        if (!notification.isRead()) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    // =========================
    // EV-417 — Mark ALL notifications as read
    // EV-418 — Scoped strictly to logged-in user
    // =========================
    @Override
    public void markAllAsRead() {

        User user = getLoggedInUser();

        try {
            notificationRepository.markAllAsReadByUserId(user.getId());
        } catch (Exception e) {
            log.warn(
                "Mark-all-notifications-read failed for userId={}",
                user.getId(),
                e
            );
        }
    }

    // =========================
    // DTO Mapper
    // =========================
    private NotificationResponseDTO toDTO(Notification n) {
        return new NotificationResponseDTO(
                n.getId(),
                n.getType(),
                n.getMessage(),
                n.getRelatedEntityId(),
                n.isRead(),
                n.getCreatedAt()
        );
    }

    // =========================
    // Helpers
    // =========================
    private User getLoggedInUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Pageable safePageable(int page, int size) {

        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 10 : Math.min(size, 50);

        return PageRequest.of(safePage, safeSize);
    }

    private Set<NotificationType> allowedTypesForRole(Role role) {

        if (role == Role.ORGANIZER) {
            return Set.of(
                    NotificationType.SMART_RULE,
                    NotificationType.EVENT
            );
        }

        return Set.of(
                NotificationType.BOOKING,
                NotificationType.LOST_FOUND,
                NotificationType.EVENT
        );
    }
}
