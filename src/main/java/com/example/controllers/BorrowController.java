package com.example.controllers;

import com.example.entity.Borrow;
import com.example.service.BorrowService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/borrows")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @GetMapping
    public ResponseEntity<List<Borrow>> getAllBorrows() {
        var borrows = borrowService.findAllBorrows();

        if (borrows.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(borrows);
    }

    @PostMapping
    public ResponseEntity<Borrow> saveBorrow(
            @Valid @RequestBody @Parameter(description = "The book to be saved") Borrow borrow) {
        var savedBorrow = borrowService.addNewBorrow(borrow);
        return ResponseEntity
                .created(URI.create(String.format("/%d", borrow.getId())))
                .body(savedBorrow);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borrow> getBorrowById(
            @PathVariable @Parameter(description = "The ID of the borrow") long id) {
        Optional<Borrow> borrow = borrowService.findByBorrowId(id);
        return borrow.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(
            @PathVariable @Parameter(description = "The ID of the borrow") long id) {
        borrowService.deleteBorrowById(id);
        return ResponseEntity.ok("Borrow deleted successfully.");
    }

}
