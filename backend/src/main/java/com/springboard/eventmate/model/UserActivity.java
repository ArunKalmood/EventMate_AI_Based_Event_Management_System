package com.springboard.eventmate.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_activity")
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Long eventId;

    @Column(nullable = false)
    private String actionType;

    private String actionValue;

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    // Getters and Setters
    
    public Long getId() {
		return id;
	}
    
    	public void setId(Long id) {
		this.id = id;
	}
    	
    	public Long getUserId() {
		return userId;
	}
    		
    	public void setUserId(Long userId) {
		this.userId = userId;
	}
    	
    	public Long getEventId() {
		return eventId;
	}
			
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
			
	public String getActionType() {
		return actionType;
	}
			
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
			
	public String getActionValue() {
		return actionValue;
	}
			
	public void setActionValue(String actionValue) {
		this.actionValue = actionValue;
	}
			
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
			
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}

