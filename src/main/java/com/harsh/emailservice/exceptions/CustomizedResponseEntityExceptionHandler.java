package com.harsh.emailservice.exceptions;

import com.harsh.emailservice.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * This class handles the exception thrown for Request Validation, and wraps the response in consistent format.
 */
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handle(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        String message = "The request body is invalid";
        List<String> errors = new ArrayList<>();
        violations.forEach(violation ->
                errors.add(violation.getMessage())
        );
        ExceptionResponse response = new ExceptionResponse(message, errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handle(MethodArgumentNotValidException exception) {

        String message = "The request body is invalid";
        BindingResult result = exception.getBindingResult();
        List<ObjectError> validationErrors = result.getAllErrors();
        List<String> errors = new ArrayList<>();
        validationErrors.forEach(validationError ->
                errors.add(validationError.getObjectName() + ": " + validationError.getDefaultMessage())
        );
        ExceptionResponse response = new ExceptionResponse(message, Arrays.asList(exception.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
