package com.springboard.eventmate.exception;

public class TicketAlreadyUsedException extends RuntimeException {
    public TicketAlreadyUsedException() {
        super("Ticket already used");
    }
}
