package com.springboard.eventmate.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboard.eventmate.model.dto.LostItemResponse;
import com.springboard.eventmate.model.dto.ReportLostItemRequest;
import com.springboard.eventmate.service.LostItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class LostItemController {

    private final LostItemService lostItemService;

    // =========================
    // REPORT LOST ITEM (USER)
    // =========================
    @PostMapping(
    	    value = "/{eventId}/lost",
    	    consumes = MediaType.APPLICATION_JSON_VALUE
    	)
    	public ResponseEntity<LostItemResponse> reportLostItem(
    	        @PathVariable Long eventId,
    	        @RequestBody ReportLostItemRequest request
    	) {
    	    LostItemResponse response =
    	            lostItemService.reportLostItem(
    	                    eventId,
    	                    request.getTitle(),
    	                    request.getDescription()
    	            );

    	    return ResponseEntity.ok(response);
    	}


    // =========================
    // GET LOST ITEMS BY EVENT
    // =========================
    @GetMapping("/{eventId}/lost")
    public ResponseEntity<List<LostItemResponse>> getLostItemsByEvent(
            @PathVariable Long eventId
    ) {
        return ResponseEntity.ok(
                lostItemService.getLostItemsByEvent(eventId)
        );
    }
    
    @GetMapping("/users/me/lost-items")
    public ResponseEntity<List<LostItemResponse>> getMyLostItems() {
        return ResponseEntity.ok(
                lostItemService.getLostItemsForCurrentUser()
        );
    }

}
