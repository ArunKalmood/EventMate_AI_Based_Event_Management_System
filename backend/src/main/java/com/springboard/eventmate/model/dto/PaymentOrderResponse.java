package com.springboard.eventmate.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.springboard.eventmate.model.enums.PaymentStatus;

public record PaymentOrderResponse(
        Long paymentId,
        Long bookingId,
        BigDecimal amount,
        String paypalOrderId,
        PaymentStatus status,
        LocalDateTime createdAt
) {}
