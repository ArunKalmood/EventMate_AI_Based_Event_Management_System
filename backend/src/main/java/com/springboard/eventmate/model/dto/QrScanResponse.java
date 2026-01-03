package com.springboard.eventmate.model.dto;

public class QrScanResponse {

    private boolean valid;
    private String message;

    public QrScanResponse(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}
