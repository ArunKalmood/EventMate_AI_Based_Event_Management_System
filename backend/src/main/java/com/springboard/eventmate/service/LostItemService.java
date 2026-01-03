package com.springboard.eventmate.service;

import java.util.List;
import java.util.Map;

import com.springboard.eventmate.model.dto.LostAndFoundSummaryDTO;
import com.springboard.eventmate.model.dto.LostItemResponse;
import com.springboard.eventmate.model.enums.LostItemStatus;

public interface LostItemService {

    // =========================
    // USER
    // =========================
    LostItemResponse reportLostItem(Long eventId, String title, String description);

    List<LostItemResponse> getLostItemsByEvent(Long eventId);

    //  NEW: USER – My Lost Items
    List<LostItemResponse> getLostItemsForCurrentUser();

    // =========================
    // ORGANIZER
    // =========================
    List<LostItemResponse> getLostItemsForOrganizer();

    LostItemResponse updateLostItemStatus(Long lostItemId, LostItemStatus status);

    Map<Long, LostAndFoundSummaryDTO> getLostAndFoundSummaryForOrganizer();

    List<LostItemResponse> getLostItemsForOrganizerEvent(
            Long eventId,
            String organizerEmail
    );
}
