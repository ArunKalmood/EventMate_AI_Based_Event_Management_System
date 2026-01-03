package com.springboard.eventmate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboard.eventmate.model.Booking;
import com.springboard.eventmate.model.Ticket;

import jakarta.persistence.LockModeType;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // One ticket per booking
    Optional<Ticket> findByBooking(Booking booking);

    // Human-readable reference lookup
    Optional<Ticket> findByTicketCode(String ticketCode);

    // QR scan lookup
    Optional<Ticket> findByQrPayload(String qrPayload);

    //  LOCKED lookup for atomic scan (EV-287)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT t FROM Ticket t
        WHERE (:qrPayload IS NOT NULL AND t.qrPayload = :qrPayload)
           OR (:ticketCode IS NOT NULL AND t.ticketCode = :ticketCode)
    """)
    Optional<Ticket> findForValidation(
            @Param("qrPayload") String qrPayload,
            @Param("ticketCode") String ticketCode
    );
}
