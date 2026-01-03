package com.springboard.eventmate.model.dto;

import jakarta.validation.constraints.NotBlank;

public class ReportLostItemRequest {

    @NotBlank
    private String title;

    private String description;

    // Constructors
    public ReportLostItemRequest() {}

    public ReportLostItemRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getters & setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
