package com.springboard.eventmate.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyPaymentRequest {

	private String paypalOrderId;
    private String paypalCaptureId;
}
