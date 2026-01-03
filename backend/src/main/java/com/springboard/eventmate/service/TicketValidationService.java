package com.springboard.eventmate.service;

import com.springboard.eventmate.model.Ticket;

public interface TicketValidationService {

    /**
     * Validates a ticket using qrPayload or ticketCode.
     * 
     * @throws RuntimeException with codes:
     *  - TICKET_NOT_FOUND
     *  - TICKET_ALREADY_USED
     *  - BOOKING_NOT_ACTIVE
     */
    Ticket validateOrThrow(String qrPayload, String ticketCode);
}
