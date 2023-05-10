package com.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class Hello {

    @GetMapping("/api/v1/welcome")
    public ResponseEntity<Object> getData() {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("message", "Welcome to the library!");
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        data.put("currentDate", formatter.format(new Date()));
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
