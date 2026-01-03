package com.springboard.eventmate.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class OrganizerEventSummaryDTO {

    private Long eventId;
    private String title;
    private LocalDate date;
    private LocalTime time;
    private String location;

    // Status: ACTIVE / FULL / CLOSED
    private String status;

    private Integer capacity;

    //  FIX: must be Long (JPA count returns long)
    private Long bookedCount;

    private Integer remainingSeats;

    // EV-430
    private Integer capacityUsagePercentage;

    // EV-436
    private String demandState;        // NONE / TRENDING / FILLING_FAST
    private Boolean overcrowdingRisk;  // true / false
}
