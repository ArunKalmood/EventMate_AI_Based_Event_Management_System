package com.springboard.eventmate.service;

import java.util.List;

import com.springboard.eventmate.model.Booking;
import com.springboard.eventmate.model.dto.BookingResponseDTO;
import com.springboard.eventmate.model.dto.OrganizerBookingResponseDTO;

public interface BookingService {

    Booking bookEvent(Long eventId, String userEmail, int quantity);

    List<BookingResponseDTO> getBookingsForCurrentUser(String userEmail);

    // EV-148: Organizer viewing bookings of their own event
    List<OrganizerBookingResponseDTO> getBookingsForEventAsOrganizer(
            Long eventId,
            String organizerEmail
    );

    // EV-242: Cancel a booking
    void cancelBooking(Long bookingId, String userEmail);
}
