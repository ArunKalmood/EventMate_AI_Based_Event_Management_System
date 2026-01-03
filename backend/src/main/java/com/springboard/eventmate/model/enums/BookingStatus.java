package com.springboard.eventmate.model.enums;

public enum BookingStatus {

    PENDING_PAYMENT, // booking created, waiting for payment
    CONFIRMED,       // payment successful
    CANCELLED        // payment failed or booking cancelled
}
