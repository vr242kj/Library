package com.example.controllers;

import com.example.service.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidArgument(MethodArgumentNotValidException exception) {
        List<Map<String, Object>> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, Object> errorMap = new LinkedHashMap<>();
                    errorMap.put("fieldName", fieldError.getField());
                    errorMap.put("invalidValue", fieldError.getRejectedValue());
                    errorMap.put("constraint", fieldError.getDefaultMessage());
                    return errorMap;
                })
                .collect(Collectors.toList());

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("dateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        responseMap.put("errorMessage", "Failed to create new book due to invalid values in request body");
        responseMap.put("errors", errors);

        return ResponseEntity.badRequest().body(responseMap);
    }
}

