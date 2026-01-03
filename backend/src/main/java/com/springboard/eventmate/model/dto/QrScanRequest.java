package com.springboard.eventmate.model.dto;

public class QrScanRequest {

    private String qrPayload;
    private String ticketCode;

    public String getQrPayload() {
        return qrPayload;
    }

    public void setQrPayload(String qrPayload) {
        this.qrPayload = qrPayload;
    }
    
    public String getTicketCode() {
		return ticketCode;
	}
    
    public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}
}
