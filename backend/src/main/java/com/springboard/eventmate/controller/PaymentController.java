package com.springboard.eventmate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboard.eventmate.model.Payment;
import com.springboard.eventmate.model.dto.PaymentOrderResponse;
import com.springboard.eventmate.model.dto.VerifyPaymentRequest;
import com.springboard.eventmate.model.enums.PaymentStatus;
import com.springboard.eventmate.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // =========================
    // CREATE PAYPAL ORDER
    // =========================
    @PostMapping("/payments/create-order")
    public ResponseEntity<PaymentOrderResponse> createPaymentOrder(
            @RequestParam Long bookingId,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();

        Payment payment =
                paymentService.createPaymentOrder(bookingId, userEmail);

        PaymentOrderResponse response =
                new PaymentOrderResponse(
                        payment.getId(),
                        payment.getBooking().getId(),
                        payment.getAmount(),
                        payment.getPaypalOrderId(),
                        payment.getStatus(),
                        payment.getCreatedAt()
                );

        return ResponseEntity.ok(response);
    }


    // =========================
    // VERIFY PAYPAL PAYMENT
    // =========================
    @PostMapping("/payments/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestBody VerifyPaymentRequest request,
            Authentication authentication
    ) {

        Payment payment =
                paymentService.verifyPayment(
                        request.getPaypalOrderId(),
                        request.getPaypalCaptureId(),
                        null
                );

        if (payment.getStatus() != PaymentStatus.PAID) {
            return ResponseEntity
                    .badRequest()
                    .body("Payment verification failed");
        }

        return ResponseEntity.ok("Payment successful. Booking confirmed.");
    }
}
