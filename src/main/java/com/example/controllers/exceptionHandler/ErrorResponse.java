package com.example.controllers.exceptionHandler;


import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private final LocalDateTime dateTime;
    private List<ErrorDetail> errors;
    private String errorMessage;

    public ErrorResponse(LocalDateTime dateTime, List<ErrorDetail> errors) {
        this.dateTime = dateTime;
        this.errors = errors;
    }

    public ErrorResponse(LocalDateTime dateTime, String errorMessage) {
        this.dateTime = dateTime;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public List<ErrorDetail> getErrors() {
        return errors;
    }
}
