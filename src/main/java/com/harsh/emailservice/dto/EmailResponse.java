package com.harsh.emailservice.dto;

/**
 * This class defines the schema for successful response for the "Send Email" api
 */
public class EmailResponse {
    private String status;

    public EmailResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
