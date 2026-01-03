package com.springboard.eventmate.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboard.eventmate.model.dto.LostItemResponse;
import com.springboard.eventmate.model.dto.UpdateLostItemStatusRequest;
import com.springboard.eventmate.service.LostItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/organizer/lost-items")
@RequiredArgsConstructor
public class OrganizerLostItemController {

    private final LostItemService lostItemService;

    // EXISTING — overview (all events)
    @GetMapping
    public ResponseEntity<List<LostItemResponse>> getOrganizerLostItems() {
        return ResponseEntity.ok(
            lostItemService.getLostItemsForOrganizer()
        );
    }

    //  STEP 3 — NEW (per event, organizer-safe)
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<LostItemResponse>> getLostItemsForEvent(
            @PathVariable Long eventId,
            Authentication authentication
    ) {
        String organizerEmail = authentication.getName();

        return ResponseEntity.ok(
                lostItemService.getLostItemsForOrganizerEvent(
                        eventId,
                        organizerEmail
                )
        );
    }

    // EXISTING — status update
    @PutMapping("/{lostItemId}/status")
    public ResponseEntity<LostItemResponse> updateLostItemStatus(
            @PathVariable Long lostItemId,
            @RequestBody UpdateLostItemStatusRequest request
    ) {
        return ResponseEntity.ok(
                lostItemService.updateLostItemStatus(
                        lostItemId,
                        request.getStatus()
                )
        );
    }
}
