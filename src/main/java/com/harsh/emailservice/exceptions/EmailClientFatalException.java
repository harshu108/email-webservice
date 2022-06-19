package com.harsh.emailservice.exceptions;

/**
 * This class represents the exceptions that occur in communication with Email endpoints. The exceptions usually means that
 * the email client is unusable for future requests.
 */
public class EmailClientFatalException extends RuntimeException {

    private String endpointMessage;

    public EmailClientFatalException(String message) {
        super(message);
    }

    public EmailClientFatalException(String message, String endpointMessage) {
        super(message);
        this.endpointMessage = endpointMessage;
    }


    public String getEndpointMessage() {
        return endpointMessage;
    }
}
