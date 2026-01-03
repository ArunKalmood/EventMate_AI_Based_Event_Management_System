package com.springboard.eventmate.model.dto;

import lombok.Data;

@Data
public class OrganizerLostAndFoundSummaryDTO {

    private Long eventId;
    private String eventTitle;

    private Integer totalLostItems;
    private Integer pendingCount;
    private Integer foundCount;
    private Integer returnedCount;
}
