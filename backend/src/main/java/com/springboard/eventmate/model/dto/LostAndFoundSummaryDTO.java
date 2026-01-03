package com.springboard.eventmate.model.dto;

import lombok.Data;

@Data
public class LostAndFoundSummaryDTO {

    private Long eventId;

    private Integer totalLostItems;
    private Integer pendingCount;
    private Integer foundCount;
    private Integer returnedCount;
}
