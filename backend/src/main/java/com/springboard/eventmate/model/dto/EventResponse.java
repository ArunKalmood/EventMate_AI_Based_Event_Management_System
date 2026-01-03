package com.springboard.eventmate.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EventResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private String location;

    private String organizerName;
    private String organizerEmail;

    // EV-167 computed fields
    private Integer capacity;
    private Long bookedCount;
    private Integer availableSeats;
    private String status;   // AVAILABLE / FILLING_FAST / FULL

    // EV-254 category
    private String category;

    // EV-255 tags
    private List<String> tags;

    // EV-271 booking count
    private int bookingCount;

    // EV-271 booked by user
    private boolean bookedByUser;

    // =========================
    // EV-389 Smart Rules signals
    // =========================
    private String demandState;                 // NONE / TRENDING / FILLING_FAST
    private Integer capacityUsagePercentage;    // %
    private Boolean overcrowdingRisk;           // true / false

    private String bannerImageUrl;

    // =========================
    //  PRICING (PAYMENT SAFE)
    // =========================
    private BigDecimal price;
    
    // EV-AI-01 — AI explanation
    private String recommendationReason;
    
    private Double avgRating;
    private Integer reviewCount;
    private Boolean isQualified;



    // ---------- Getters & Setters ----------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }

    public String getOrganizerEmail() { return organizerEmail; }
    public void setOrganizerEmail(String organizerEmail) { this.organizerEmail = organizerEmail; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Long getBookedCount() { return bookedCount; }
    public void setBookedCount(Long bookedCount) { this.bookedCount = bookedCount; }

    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public int getBookingCount() { return bookingCount; }
    public void setBookingCount(int bookingCount) { this.bookingCount = bookingCount; }

    public boolean isBookedByUser() { return bookedByUser; }
    public void setBookedByUser(boolean bookedByUser) { this.bookedByUser = bookedByUser; }

    public String getDemandState() { return demandState; }
    public void setDemandState(String demandState) { this.demandState = demandState; }

    public Integer getCapacityUsagePercentage() { return capacityUsagePercentage; }
    public void setCapacityUsagePercentage(Integer capacityUsagePercentage) {
        this.capacityUsagePercentage = capacityUsagePercentage;
    }

    public Boolean getOvercrowdingRisk() { return overcrowdingRisk; }
    public void setOvercrowdingRisk(Boolean overcrowdingRisk) {
        this.overcrowdingRisk = overcrowdingRisk;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }
    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    //  PRICE
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getRecommendationReason() {
		return recommendationReason;
	}
    
    	public void setRecommendationReason(String recommendationReason) {
		this.recommendationReason = recommendationReason;
	}
    	
    	public Double getAvgRating() {
		return avgRating;
	}
   
    	public void setAvgRating(Double avgRating) {
		this.avgRating = avgRating;
	}
    	
    	public Integer getReviewCount() {
		return reviewCount;
	}
    	
    	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}
    	
    	public Boolean getIsQualified() {
		return isQualified;
	}
		
	public void setIsQualified(Boolean isQualified) {
		this.isQualified = isQualified;
	}
	
	
}
