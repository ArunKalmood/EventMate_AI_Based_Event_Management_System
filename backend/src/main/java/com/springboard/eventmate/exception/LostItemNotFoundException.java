package com.springboard.eventmate.exception;

public class LostItemNotFoundException extends RuntimeException {

    public LostItemNotFoundException(String message) {
        super(message);
    }
}
