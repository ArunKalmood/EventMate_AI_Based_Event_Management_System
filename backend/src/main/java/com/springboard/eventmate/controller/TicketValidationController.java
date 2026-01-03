package com.springboard.eventmate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboard.eventmate.model.dto.QrScanRequest;
import com.springboard.eventmate.model.dto.QrScanResponse;
import com.springboard.eventmate.service.TicketValidationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketValidationController {

    private final TicketValidationService ticketValidationService;

    @PostMapping("/validate")
    public ResponseEntity<QrScanResponse> validate(@RequestBody QrScanRequest request) {

        // Delegate validation to service (throws if invalid)
        ticketValidationService.validateOrThrow(
                request.getQrPayload(),
                request.getTicketCode()
        );

        // If no exception → entry is allowed
        return ResponseEntity.ok(
                new QrScanResponse(true, "Entry allowed")
        );
    }
}
