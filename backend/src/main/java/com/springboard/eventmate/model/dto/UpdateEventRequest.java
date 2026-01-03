package com.springboard.eventmate.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.springboard.eventmate.model.enums.EventCategory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateEventRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime time;

    @NotBlank
    private String location;

    @NotNull
    @Min(0)
    private Integer capacity;

    //  NEW — EVENT PRICE
    @NotNull
    @Min(0)
    private BigDecimal price;

    @Size(max = 50, message = "category max length is 50")
    private String category;

    @Size(max = 10, message = "max 10 tags allowed")
    private List<
        @NotBlank(message = "tag cannot be blank")
        @Size(max = 30, message = "tag max length is 30")
        String
    > tags;

    // =====================
    // Getters & Setters
    // =====================

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

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(@NotNull @Min(0) BigDecimal price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    // =========================
    // NORMALIZED ACCESSOR
    // =========================
    public String getNormalizedCategory() {
        if (category == null || category.isBlank()) {
            return EventCategory.OTHER.name();
        }
        return EventCategory.fromString(category).name();
    }
}
