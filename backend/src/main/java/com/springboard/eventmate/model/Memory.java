package com.springboard.eventmate.model;

import java.time.LocalDateTime;

import com.springboard.eventmate.model.enums.MediaType;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "memories")
@Getter
@Setter
@NoArgsConstructor
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Uploader
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Related Event
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // Cloudinary public URL
    @Column(nullable = false)
    private String mediaUrl;

    // IMAGE / VIDEO
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType mediaType;

    // Optional caption
    @Column(length = 500)
    private String caption;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
