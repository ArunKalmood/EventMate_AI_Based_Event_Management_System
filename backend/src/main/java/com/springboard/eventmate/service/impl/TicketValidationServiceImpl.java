package com.springboard.eventmate.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboard.eventmate.exception.BookingNotActiveException;
import com.springboard.eventmate.exception.TicketAlreadyUsedException;
import com.springboard.eventmate.exception.TicketNotFoundException;
import com.springboard.eventmate.model.Ticket;
import com.springboard.eventmate.model.enums.BookingStatus;
import com.springboard.eventmate.repository.TicketRepository;
import com.springboard.eventmate.service.TicketValidationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketValidationServiceImpl implements TicketValidationService {

    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public Ticket validateOrThrow(String qrPayload, String ticketCode) {

        // Locked fetch (prevents concurrent success)
        Ticket ticket = ticketRepository
                .findForValidation(qrPayload, ticketCode)
                .orElseThrow(TicketNotFoundException::new);

        // Already used
        if (ticket.isUsed()) {
            throw new TicketAlreadyUsedException();
        }

        // Booking must be CONFIRMED
        if (ticket.getBooking().getStatus() != BookingStatus.CONFIRMED) {
            throw new BookingNotActiveException();
        }

        // EV-287: atomic usage marking
        ticket.setUsed(true);
        ticketRepository.save(ticket);

        return ticket;
    }
}
