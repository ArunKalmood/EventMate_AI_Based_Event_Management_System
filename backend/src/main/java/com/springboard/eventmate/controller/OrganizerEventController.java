package com.springboard.eventmate.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboard.eventmate.model.dto.EventSummaryResponseDTO;
import com.springboard.eventmate.model.dto.OrganizerEventSummaryDTO;
import com.springboard.eventmate.model.dto.OrganizerLostAndFoundSummaryDTO;
import com.springboard.eventmate.service.EventService;
import com.springboard.eventmate.service.LostFoundOverviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/organizer/events")
@RequiredArgsConstructor
public class OrganizerEventController {

	private final EventService eventService;
	private final LostFoundOverviewService lostFoundOverviewService;


	// =====================================================
	// LEGACY / DETAILED SUMMARY (EV-173)
	// Purpose: analytics-rich organizer view
	// =====================================================
	@GetMapping("/summary")
	public ResponseEntity<List<EventSummaryResponseDTO>> getEventSummary(Authentication auth) {

		String organizerEmail = auth.getName();

		List<EventSummaryResponseDTO> summary = eventService.getOrganizerEventSummary(organizerEmail);

		return ResponseEntity.ok(summary);
	}

	// =====================================================
	// EV-426 — ORGANIZER DASHBOARD OVERVIEW (CANONICAL)
	// Purpose: lightweight, summary-only dashboard list
	// =====================================================
	@GetMapping
	public ResponseEntity<Page<OrganizerEventSummaryDTO>> getOrganizerEvents(Authentication auth,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		String organizerEmail = auth.getName();

		Page<OrganizerEventSummaryDTO> result = eventService.getOrganizerEventSummaries(organizerEmail, page, size);

		return ResponseEntity.ok(result);
	}
	
	// =====================================================
	// EV-444 — LOST & FOUND OVERVIEW (ORGANIZER)
	// Purpose: read-only Lost & Found summary per event
	// =====================================================
	@GetMapping("/lost-found")
	public ResponseEntity<Page<OrganizerLostAndFoundSummaryDTO>> getLostFoundOverview(
	        Authentication auth,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size
	) {
	    return ResponseEntity.ok(
	            lostFoundOverviewService.getOrganizerLostFoundOverview(page, size)
	    );
	}

}
