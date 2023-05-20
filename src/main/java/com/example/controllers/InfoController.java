package com.example.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class InfoController {

    @GetMapping("/welcome")
    public ResponseEntity<Map<String, String>> getWelcomeInfo() {

        var info = Map.of("message", "Welcome to the library!",
                "currentDate", DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDateTime.now()));

        return ResponseEntity.ok(info);
    }
}
