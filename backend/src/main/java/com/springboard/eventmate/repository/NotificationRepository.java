package com.springboard.eventmate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.springboard.eventmate.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // EV-351 — Prevent duplicate FOUND notifications
    boolean existsByUserIdAndLostItemIdAndTitle(
            Long userId,
            Long lostItemId,
            String title
    );
    
    //  EV-397 — fetch all notifications for a user
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    //  EV-397 — fetch unread notifications
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
    
    boolean existsByUserIdAndEventIdAndTitle(
    	    Long userId,
    	    Long eventId,
    	    String title
    	);
    
    Page<Notification> findByUserIdOrderByCreatedAtDesc(
            Long userId,
            Pageable pageable
    );

    Page<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(
            Long userId,
            Pageable pageable
    );
    
    Optional<Notification> findByIdAndUserId(Long id, Long userId);
    
    @Transactional
    @Modifying
    @Query("""
        update Notification n
        set n.isRead = true
        where n.user.id = :userId
          and n.isRead = false
    """)
    int markAllAsReadByUserId(Long userId);

}
