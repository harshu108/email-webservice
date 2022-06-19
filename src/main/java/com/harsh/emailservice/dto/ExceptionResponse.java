package com.harsh.emailservice.dto;

import java.util.List;

public class ExceptionResponse extends RuntimeException {

    private final List<String> errors;

    public ExceptionResponse(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

}
