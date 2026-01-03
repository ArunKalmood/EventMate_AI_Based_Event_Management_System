package com.springboard.eventmate.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboard.eventmate.model.Booking;
import com.springboard.eventmate.model.dto.BookingResponseDTO;
import com.springboard.eventmate.model.dto.OrganizerBookingResponseDTO;
import com.springboard.eventmate.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/events/{id}/book")
    public ResponseEntity<String> bookEvent(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int quantity,
            Authentication authentication
    ) {

        String email = authentication.getName();

        Booking booking = bookingService.bookEvent(id, email, quantity);

        return ResponseEntity
                .status(201)
                .body("Booking successful. Booking ID: " + booking.getId());
    }

    // EV-144 — Get current user's bookings
    @GetMapping("/bookings/me")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(
            Authentication authentication
    ) {

        String userEmail = authentication.getName();

        List<BookingResponseDTO> bookings =
                bookingService.getBookingsForCurrentUser(userEmail);

        return ResponseEntity.ok(bookings);
    }

    // EV-150: Organizer view bookings for their event
    @GetMapping("/organizer/bookings")
    public ResponseEntity<List<OrganizerBookingResponseDTO>> getOrganizerBookings(
            @RequestParam Long eventId,
            Authentication authentication
    ) {

        String organizerEmail = authentication.getName();

        List<OrganizerBookingResponseDTO> bookings =
                bookingService.getBookingsForEventAsOrganizer(
                        eventId,
                        organizerEmail
                );

        return ResponseEntity.ok(bookings);
    }

    // EV-242: Cancel a booking
    @PostMapping("/bookings/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable Long bookingId,
            Authentication authentication
    ) {

        String userEmail = authentication.getName();

        bookingService.cancelBooking(bookingId, userEmail);

        return ResponseEntity.noContent().build();
    }
}
