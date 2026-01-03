package com.springboard.eventmate.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerBookingResponseDTO {

    // user info
    private String userName;
    private String userEmail;

    // event info
    private Long eventId;
    private String eventTitle;
    private LocalDate eventDate;
    private LocalTime eventTime;

    // booking info
    private LocalDateTime bookedAt;
    
    // booking status: CONFIRMED / CANCELLED
    private String status;
    
}
