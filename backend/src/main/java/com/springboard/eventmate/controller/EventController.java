package com.springboard.eventmate.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.dto.CreateEventRequest;
import com.springboard.eventmate.model.dto.EventResponse;
import com.springboard.eventmate.model.dto.EventSummaryResponseDTO;
import com.springboard.eventmate.model.dto.UpdateEventRequest;
import com.springboard.eventmate.model.enums.EventCategory;
import com.springboard.eventmate.service.EventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.springboard.eventmate.service.UserActivityService;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final UserActivityService userActivityService;

    // CREATE EVENT
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Event> createEvent(
            @Valid @ModelAttribute CreateEventRequest request,
            Authentication auth) {

        String email = auth.getName();
        Event created = eventService.createEvent(request, email);
        return ResponseEntity.ok(created);
    }

    // GET ALL EVENTS
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents(Authentication auth) {

        String email = auth != null ? auth.getName() : null;
        return ResponseEntity.ok(eventService.getAllEvents(email));
    }

    // GET EVENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(
            @PathVariable Long id,
            Authentication auth) {

        String email = auth != null ? auth.getName() : null;

        EventResponse event = eventService.getEventById(id, email);

        if (auth != null) {
	        	userActivityService.log(
	        		    email,
	        		    id,
	        		    "VIEW_EVENT",
	        		    event.getCategory()
	        		);
        }

        return ResponseEntity.ok(event);
    }

    
    // UPDATE EVENT
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEventRequest request,
            Authentication auth) {

        String email = auth.getName();
        Event updated = eventService.updateEvent(id, request, email);
        return ResponseEntity.ok(updated);
    }

    // DELETE EVENT
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(
            @PathVariable Long id,
            Authentication auth) {

        String email = auth.getName();
        eventService.deleteEvent(id, email);
        return ResponseEntity.ok("Event deleted successfully");
    }

    // CLOSE EVENT
    @PutMapping("/{id}/close")
    public ResponseEntity<String> closeEvent(
            @PathVariable Long id,
            Authentication auth) {

        String email = auth.getName();
        eventService.closeEvent(id, email);
        return ResponseEntity.ok("Event closed successfully");
    }

    //  FIXED: SEARCH EVENTS
    @GetMapping("/search")
    public ResponseEntity<Page<EventSummaryResponseDTO>> searchEvents(
            @RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication auth) {

        if (page < 0) throw new IllegalArgumentException("Page must be 0 or greater");
        if (size <= 0) throw new IllegalArgumentException("Size must be greater than 0");
        if (size > 50) throw new IllegalArgumentException("Size must not exceed 50");

        String email = auth != null ? auth.getName() : null;

        String normalizedCategory =
        		(category == null || category.isBlank())
        		? null
        				: EventCategory.fromString(category).name();
        //  LOG SEARCH (ONLY IF REAL QUERY)
        if (auth != null && q != null && !q.isBlank() && normalizedCategory != null) {
	        	userActivityService.log(
	        		    email,
	        		    null,
	        		    "SEARCH",
	        		    normalizedCategory
	        		);

        }


        Page<EventSummaryResponseDTO> results =
                eventService.searchEvents(q, normalizedCategory, page, size, email);

        return ResponseEntity.ok(results);
    }
    
	 // =========================
	 // CURATED EVENTS (AI + RULES)
	 // =========================
    
    @GetMapping("/curated")
    public ResponseEntity<List<EventResponse>> getCuratedEvents(
            Authentication auth) {

        if (auth == null) {
            return ResponseEntity.ok(List.of());
        }

        String email = auth.getName();
        return ResponseEntity.ok(eventService.getCuratedEvents(email));
    }
    
    

}
