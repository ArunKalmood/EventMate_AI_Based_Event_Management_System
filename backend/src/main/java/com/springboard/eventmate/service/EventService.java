package com.springboard.eventmate.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.dto.CreateEventRequest;
import com.springboard.eventmate.model.dto.EventResponse;
import com.springboard.eventmate.model.dto.EventSummaryResponseDTO;
import com.springboard.eventmate.model.dto.OrganizerEventSummaryDTO;
import com.springboard.eventmate.model.dto.UpdateEventRequest;

public interface EventService {

    Event createEvent(CreateEventRequest request, String userEmail);

    List<EventResponse> getAllEvents(String userEmail);

    EventResponse getEventById(Long id, String userEmail);
    
    List<EventResponse> getCuratedEvents(String userEmail);


    Event updateEvent(Long eventId, UpdateEventRequest request, String userEmail);
    

    void deleteEvent(Long eventId, String userEmail);

    List<EventSummaryResponseDTO> getOrganizerEventSummary(String organizerEmail);

    void closeEvent(Long eventId, String organizerEmail);

    // EV-249: Search events by keyword (title + location) with pagination
    Page<EventSummaryResponseDTO> searchEvents(
    	    String keyword,
    	    String category,
    	    int page,
    	    int size,
    	    String userEmail
    );
    
    List<OrganizerEventSummaryDTO> getOrganizerEventSummaries(String organizerEmail);
    
    Page<OrganizerEventSummaryDTO> getOrganizerEventSummaries(
            String organizerEmail,
            int page,
            int size
    );
    
    EventResponse toEventResponse(Event event, String userEmail);




}
