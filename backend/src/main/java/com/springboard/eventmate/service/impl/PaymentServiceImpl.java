package com.springboard.eventmate.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboard.eventmate.exception.BookingNotFoundException;
import com.springboard.eventmate.model.Booking;
import com.springboard.eventmate.model.Payment;
import com.springboard.eventmate.model.Ticket;
import com.springboard.eventmate.model.User;
import com.springboard.eventmate.model.enums.BookingStatus;
import com.springboard.eventmate.model.enums.PaymentStatus;
import com.springboard.eventmate.repository.BookingRepository;
import com.springboard.eventmate.repository.PaymentRepository;
import com.springboard.eventmate.repository.TicketRepository;
import com.springboard.eventmate.repository.UserRepository;
import com.springboard.eventmate.service.PayPalAuthService;
import com.springboard.eventmate.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PayPalAuthService payPalAuthService;
    private final TicketRepository ticketRepository;


    // =========================
    // CREATE PAYPAL ORDER (PRE-PAYMENT)
    // =========================
    @Override
    @Transactional
    public Payment createPaymentOrder(Long bookingId, String userEmail) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        if (booking.getTotalAmount() == null ||
            booking.getTotalAmount().compareTo(BigDecimal.ZERO) == 0) {

            throw new IllegalStateException(
                "Free bookings must not be processed via PayPal"
            );
        }

        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Payment already processed for this booking");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = payPalAuthService.getAccessToken();

        String paypalOrderId = payPalAuthService.createOrder(
                accessToken,
                booking.getTotalAmount()
        );

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setUser(user);
        payment.setAmount(booking.getTotalAmount());
        payment.setPaypalOrderId(paypalOrderId);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    // =========================
    // VERIFY PAYPAL PAYMENT
    // =========================
    @Override
    @Transactional
    public Payment verifyPayment(
            String paypalOrderId,
            String ignoredCaptureId,
            String ignored
    ) {

        Payment payment = paymentRepository
                .findByPaypalOrderId(paypalOrderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        //  Idempotency guard
        if (payment.getStatus() == PaymentStatus.PAID) {
            return payment;
        }

        Booking booking = payment.getBooking();

        // Safety check
        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Booking already finalized");
        }

        if (payment.getAmount() == null ||
            payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Invalid payment amount");
        }

        //  FINALIZE BOOKING
        payment.setStatus(PaymentStatus.PAID);

        booking.setStatus(BookingStatus.CONFIRMED);

        //  Generate ticket (same behavior as free booking)
        ensureTicketExists(booking);

        bookingRepository.save(booking);
        return paymentRepository.save(payment);
    }
    
    private void ensureTicketExists(Booking booking) {
        ticketRepository.findByBooking(booking).orElseGet(() -> {
            Ticket ticket = new Ticket();
            ticket.setBooking(booking);
            ticket.setTicketCode(UUID.randomUUID().toString());
            ticket.setQrPayload(
                "EVT:" + booking.getEvent().getId()
              + "|BKG:" + booking.getId()
              + "|USR:" + booking.getUser().getId()
            );
            ticket.setCreatedAt(LocalDateTime.now());
            return ticketRepository.save(ticket);
        });
    }


}
