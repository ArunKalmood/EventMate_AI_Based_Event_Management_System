package com.springboard.eventmate.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboard.eventmate.model.Booking;
import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.User;
import com.springboard.eventmate.model.enums.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByUserAndEvent(User user, Event event);

    // EV-141: Fetch all bookings for a specific user
    List<Booking> findByUser(User user);

    List<Booking> findByEvent(Event event);

    long countByEvent(Event event);

    long countByEventId(Long eventId);

    long countByEventAndStatus(Event event, BookingStatus status);

    //  NEW: Sum of booked seats (quantity-based)
    @Query("""
        SELECT COALESCE(SUM(b.quantity), 0)
        FROM Booking b
        WHERE b.event = :event
          AND b.status = :status
    """)
    long sumQuantityByEventAndStatus(
            @Param("event") Event event,
            @Param("status") BookingStatus status
    );

    // EV-271: Check if current user has a CONFIRMED booking
    boolean existsByUserAndEventAndStatus(
            User user,
            Event event,
            BookingStatus status
    );

    long countByEventAndStatusAndBookedAtAfter(
            Event event,
            BookingStatus status,
            LocalDateTime bookedAfter
    );

    // EV-403 — fetch users booked for an event
    List<Booking> findByEventAndStatus(Event event, BookingStatus status);
    

	boolean existsByUserIdAndEventIdAndStatus(Long userId, Long eventId, BookingStatus pendingPayment);
	
	Optional<Booking> findByUserIdAndEventIdAndStatus(
		    Long userId,
		    Long eventId,
		    BookingStatus status
		);
	
	Optional<Booking> findByUserAndEventAndStatus(
	        User user,
	        Event event,
	        BookingStatus status
	);



}
