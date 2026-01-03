package com.springboard.eventmate.chat.dto;

public class ChatEventDTO {

    private Long id;
    private String title;
    private String reason;

    public ChatEventDTO() {}

    public ChatEventDTO(Long id, String title, String reason) {
        this.id = id;
        this.title = title;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
