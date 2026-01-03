package com.springboard.eventmate.model;

import java.time.LocalDateTime;

import com.springboard.eventmate.model.enums.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
	    name = "notifications",
	    indexes = {
	        @Index(name = "idx_notification_user", columnList = "user_id"),
	        @Index(name = "idx_notification_is_read", columnList = "isRead")
	    }
	)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;   // receiver (reportedBy)

    //  EV-352 references
    @Column(nullable = false)
    private Long eventId;

    @Column(nullable = true)
    private Long lostItemId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private boolean isRead = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type = NotificationType.LOST_FOUND;

    // Generic reference for booking / smart rules / event updates
    @Column(nullable = true)
    private Long relatedEntityId;
    
    

}
