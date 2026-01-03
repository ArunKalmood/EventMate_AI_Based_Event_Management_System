package com.springboard.eventmate.service.dto;

import java.math.BigDecimal;

public record PayPalCaptureResult(
        String captureId,
        BigDecimal amount,
        String currency
) {}
