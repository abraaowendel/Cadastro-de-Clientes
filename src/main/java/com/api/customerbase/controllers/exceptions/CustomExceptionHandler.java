package com.api.customerbase.controllers.exceptions;

import com.api.customerbase.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException error){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse();
        response.setStatus(status.value());
        response.setMessage(error.getMessage());
        response.setTimeStamp(Instant.now());
        return ResponseEntity.status(status).body(response);
    }
    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException error) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse();
        response.setStatus(status.value());
        response.setMessage(error.getMessage());
        response.setTimeStamp(Instant.now());
        return ResponseEntity.status(status).body(response);
    }
}
