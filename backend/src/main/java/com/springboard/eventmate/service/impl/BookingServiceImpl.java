package com.springboard.eventmate.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboard.eventmate.exception.AccessDeniedException;
import com.springboard.eventmate.exception.BookingAlreadyCancelledException;
import com.springboard.eventmate.exception.BookingNotFoundException;
import com.springboard.eventmate.exception.DuplicateBookingException;
import com.springboard.eventmate.exception.EventFullException;
import com.springboard.eventmate.exception.EventNotFoundException;
import com.springboard.eventmate.model.Booking;
import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.Notification;
import com.springboard.eventmate.model.Ticket;
import com.springboard.eventmate.model.User;
import com.springboard.eventmate.model.dto.BookingResponseDTO;
import com.springboard.eventmate.model.dto.OrganizerBookingResponseDTO;
import com.springboard.eventmate.model.enums.BookingStatus;
import com.springboard.eventmate.model.enums.EventStatus;
import com.springboard.eventmate.model.enums.NotificationType;
import com.springboard.eventmate.repository.BookingRepository;
import com.springboard.eventmate.repository.EventRepository;
import com.springboard.eventmate.repository.NotificationRepository;
import com.springboard.eventmate.repository.TicketRepository;
import com.springboard.eventmate.repository.UserRepository;
import com.springboard.eventmate.service.BookingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final NotificationRepository notificationRepository;

    // =========================
    // BOOK EVENT
    // =========================
    @Override
    @Transactional
    public Booking bookEvent(Long eventId, String userEmail, int quantity) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findByIdForUpdate(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        if (event.getStatus() == EventStatus.CLOSED) {
            throw new EventFullException("This event is closed for booking");
        }

        boolean alreadyBooked =
                bookingRepository.existsByUserAndEventAndStatus(
                        user, event, BookingStatus.CONFIRMED
                );

        if (alreadyBooked) {
            throw new DuplicateBookingException("You have already booked this event");
        }

        long confirmedSeats =
                bookingRepository.sumQuantityByEventAndStatus(
                        event, BookingStatus.CONFIRMED
                );

        if (event.getCapacity() != null &&
                confirmedSeats + quantity > event.getCapacity()) {
            throw new EventFullException("Not enough seats available");
        }

        //  NEW: snapshot pricing
        BigDecimal priceAtBooking = event.getPrice();
        BigDecimal totalAmount =
                priceAtBooking.multiply(BigDecimal.valueOf(quantity));
        
	     // =========================
	     // FREE EVENT FLOW (NO PAYMENT)
	     // =========================
        
        if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setEvent(event);
            booking.setQuantity(quantity);
            booking.setPriceAtBooking(priceAtBooking);
            booking.setTotalAmount(BigDecimal.ZERO);
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.setBookedAt(LocalDateTime.now());

            Booking saved = bookingRepository.save(booking);

            //  Create ticket immediately
            ensureTicketExists(saved);

            //  Notification
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setType(NotificationType.BOOKING);
            notification.setTitle("Booking Confirmed");
            notification.setMessage(
                    "Your free booking for \"" + event.getTitle() + "\" is confirmed."
            );
            notification.setEventId(event.getId());
            notification.setLostItemId(0L);
            notification.setRelatedEntityId(saved.getId());
            notification.setRead(false);

            try {
                notificationRepository.save(notification);
            } catch (Exception e) {
                log.warn("Booking notification failed for free booking {}", saved.getId(), e);
            }

            return saved;
        }
        
        Optional<Booking> pending =
                bookingRepository.findByUserAndEventAndStatus(
                        user,
                        event,
                        BookingStatus.PENDING_PAYMENT
                );

        if (pending.isPresent()) {
            //  Reuse existing pending booking
            return pending.get();
        }


        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setQuantity(quantity);
        booking.setPriceAtBooking(priceAtBooking);   //  NEW
        booking.setTotalAmount(totalAmount);         //  NEW
        booking.setStatus(BookingStatus.PENDING_PAYMENT);
        booking.setBookedAt(LocalDateTime.now());

        Booking saved = bookingRepository.save(booking);

        //  REMOVE: ticket must be created only after payment success
        // ensureTicketExists(saved);

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(NotificationType.BOOKING);

        //  UPDATED notification
        notification.setTitle("Payment Pending");
        notification.setMessage(
                "Please complete payment to confirm your booking for \"" +
                event.getTitle() + "\"."
        );

        notification.setEventId(event.getId());
        notification.setLostItemId(0L);
        notification.setRelatedEntityId(saved.getId());
        notification.setRead(false);

        try {
            notificationRepository.save(notification);
        } catch (Exception e) {
            log.warn("Booking notification failed for bookingId={}", saved.getId(), e);
        }

        //  REMOVE: event status changes only after CONFIRMED booking
        // recomputeEventStatus(event);

        return saved;
    }


    // =========================
    // CANCEL BOOKING
    // =========================
    @Override
    @Transactional
    public void cancelBooking(Long bookingId, String userEmail) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You can cancel only your own booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingAlreadyCancelledException("Booking already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        recomputeEventStatus(booking.getEvent());
    }

    // =========================
    // USER BOOKINGS
    // =========================
    @Override
    public List<BookingResponseDTO> getBookingsForCurrentUser(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByUser(user).stream()
                .map(b -> {
                    Ticket ticket = ticketRepository.findByBooking(b).orElse(null);

                    return new BookingResponseDTO(
                            b.getId(),
                            b.getEvent().getId(),
                            b.getEvent().getTitle(),
                            b.getEvent().getDate(),
                            b.getEvent().getTime(),
                            b.getEvent().getLocation(),
                            b.getBookedAt(),
                            b.getStatus().name(),
                            ticket != null ? ticket.getId() : null,
                            ticket != null ? ticket.getTicketCode() : null,
                            ticket != null ? ticket.getQrPayload() : null
                    );
                })
                .collect(Collectors.toList());
    }

    // =========================
    // ORGANIZER VIEW
    // =========================
    @Override
    public List<OrganizerBookingResponseDTO> getBookingsForEventAsOrganizer(
            Long eventId, String organizerEmail) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        if (!event.getCreatedBy().getEmail().equals(organizerEmail)) {
            throw new AccessDeniedException("You are not the organizer of this event");
        }

        return bookingRepository.findByEvent(event).stream()
                .map(b -> new OrganizerBookingResponseDTO(
                        b.getUser().getName(),
                        b.getUser().getEmail(),
                        event.getId(),
                        event.getTitle(),
                        event.getDate(),
                        event.getTime(),
                        b.getBookedAt(),
                        b.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    // =========================
    // STATE MACHINE CORE
    // =========================
    private void recomputeEventStatus(Event event) {

        if (event.getStatus() == EventStatus.CLOSED) {
            return;
        }

        long confirmedSeats =
                bookingRepository.sumQuantityByEventAndStatus(
                        event, BookingStatus.CONFIRMED
                );

        int capacity = event.getCapacity() == null ? 0 : event.getCapacity();

        if (capacity == 0 || confirmedSeats < capacity) {
            event.setStatus(EventStatus.ACTIVE);
        } else {
            event.setStatus(EventStatus.FULL);
        }

        eventRepository.save(event);
    }

    // =========================
    // EV-281 — Secure Ticket Code	
    // =========================
    private String generateSecureTicketCode() {
        return UUID.randomUUID().toString();
    }

    private String generateQrPayload(Booking booking) {
        return "EVT:" + booking.getEvent().getId()
             + "|BKG:" + booking.getId()
             + "|USR:" + booking.getUser().getId();
    }

    private void ensureTicketExists(Booking booking) {

        ticketRepository.findByBooking(booking).orElseGet(() -> {
            Ticket ticket = new Ticket();
            ticket.setBooking(booking);
            ticket.setTicketCode(generateSecureTicketCode());
            ticket.setQrPayload(generateQrPayload(booking));
            ticket.setCreatedAt(LocalDateTime.now());
            return ticketRepository.save(ticket);
        });
    }
}
