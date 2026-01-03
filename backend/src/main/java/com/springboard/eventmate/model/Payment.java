package com.springboard.eventmate.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.springboard.eventmate.model.enums.PaymentStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -------------------------
    // Razorpay (legacy / unused)
    // -------------------------
    @Column(unique = true)
    private String razorpayOrderId;

    @Column(unique = true)
    private String razorpayPaymentId;

    // -------------------------
    // PayPal (active gateway)
    // -------------------------
    @Column(name = "paypal_order_id", unique = true)
    private String paypalOrderId;

    @Column(name = "paypal_capture_id", unique = true)
    private String paypalCaptureId;

    // -------------------------
    // Common payment data
    // -------------------------

    // Total amount (snapshot from booking.totalAmount)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    // Booking for which payment is made
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    // User who initiated the payment
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Payment lifecycle
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    // Audit
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
