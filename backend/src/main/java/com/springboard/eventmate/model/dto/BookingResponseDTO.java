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
public class BookingResponseDTO {

	private Long bookingId;
    private Long eventId;
    private String title;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private LocalDateTime bookedAt;

    // booking status: CONFIRMED / CANCELLED
    private String status;

    //  EV-283 required fields
    private Long ticketId;
    private String ticketCode;
    private String qrPayload;
}
