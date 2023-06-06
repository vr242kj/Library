package com.example.controllers.exceptionHandler;


import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private final LocalDateTime dateTime;
    private final List<ErrorDetail> errors;

    public ErrorResponse(LocalDateTime dateTime, List<ErrorDetail> errors) {
        this.dateTime = dateTime;
        this.errors = errors;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public List<ErrorDetail> getErrors() {
        return errors;
    }
}
