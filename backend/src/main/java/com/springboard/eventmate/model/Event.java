package com.springboard.eventmate.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboard.eventmate.model.enums.EventStatus;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "events")
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private String location;

    @Column(nullable = false)
    private Integer capacity = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status = EventStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = true)
    @JsonIgnore
    private User createdBy;
    
    @Column(length = 50)
    private String category;
    
    @ElementCollection
    @Column(name = "tags")
    private List<String> tags;
    
    //  STEP 1: Event banner image
    @Column(name = "banner_image_url")
    private String bannerImageUrl;
    
    @Column(nullable = false)
    private BigDecimal price;


}
