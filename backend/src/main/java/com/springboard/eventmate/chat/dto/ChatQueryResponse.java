package com.springboard.eventmate.chat.dto;

import java.util.List;

public class ChatQueryResponse {

    private String reply;
    private List<ChatEventDTO> events;

    public ChatQueryResponse() {}

    public ChatQueryResponse(String reply, List<ChatEventDTO> events) {
        this.reply = reply;
        this.events = events;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public List<ChatEventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<ChatEventDTO> events) {
        this.events = events;
    }
}
