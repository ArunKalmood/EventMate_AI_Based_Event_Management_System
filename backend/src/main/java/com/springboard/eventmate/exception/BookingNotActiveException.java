package com.springboard.eventmate.exception;

public class BookingNotActiveException extends RuntimeException {
    public BookingNotActiveException() {
        super("Booking is not active");
    }
}

