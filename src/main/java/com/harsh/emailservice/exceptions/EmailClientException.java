package com.harsh.emailservice.exceptions;

/**
 * This class represents the exception thrown by EmailClient. These are non-fatal exceptions, meaning that the client
 * has errored because of some fault at endpoint.
 */
public class EmailClientException extends RuntimeException {

    private String message;
    public EmailClientException(String message, Throwable cause) {
        super(message);
    }

    public EmailClientException(String message) {
        super(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
