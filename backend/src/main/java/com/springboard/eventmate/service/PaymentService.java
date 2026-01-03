package com.springboard.eventmate.service;

import com.springboard.eventmate.model.Payment;

public interface PaymentService {

    // Booking-first payment creation
    Payment createPaymentOrder(Long bookingId, String userEmail);

    // PayPal verification
    Payment verifyPayment(
            String paypalOrderId,
            String paypalCaptureId,
            String ignored
    );
}
