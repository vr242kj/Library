package com.example.controllers;

import com.example.entity.Borrow;
import com.example.service.BorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/borrows")
@Tag(name = "Borrow API", description = "Endpoints for managing borrows")
public class BorrowController {
    private final BorrowService borrowService;

    @Operation(summary = "Retrieve all borrows", description = "Retrieves all borrows")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "[{\"id\": 1, \"bookId\": 1, \"readerId\": 1,"
                            + "\"borrowStartDate\": \"2023-07-24\", \"borrowEndDate\": null, \"expectedReturn\": \"2023-08-07\"},"
                            + "{\"id\": 2, \"bookId\": 2, \"readerId\": 2, "
                            + "\"borrowStartDate\": \"2023-07-20\", \"borrowEndDate\": null, \"expectedReturn\": \"2023-08-07\"}]")))
    @ApiResponse(responseCode = "204", description = "No Content", content = @Content)
    @GetMapping
    public ResponseEntity<List<Borrow>> getAllBorrows() {
        var borrows = borrowService.findAllBorrows();

        if (borrows.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(borrows);
    }

    @Operation(summary = "Save a borrow", description = "Saves a new borrow")
    @ApiResponse(responseCode = "201", description = "Created",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"id\": 1, \"bookId\": 1, \"readerId\": 1, "
                            + "\"borrowStartDate\": \"2023-07-24\", \"borrowEndDate\": null, \"expectedReturn\": \"2023-08-07\"}")))
    @ApiResponse(responseCode = "404", description = "Not Found",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"dateTime\":\"2023-07-09T23:14:00.0304944\","
                            + "\"errorMessage\":\"An error occurred: This book id doesn't exist\"}")))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"dateTime\":\"2023-07-09T23:14:00.0304944\","
                            + "\"errorMessage\":\"An error occurred: Rejected! Book isn't available\"}")))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of the Item to be created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {@ExampleObject(value = "{\"id\": 1, \"bookId\": 1, \"readerId\": 1, "
                            + "\"borrowStartDate\": \"2023-07-24\", \"borrowEndDate\": null, \"expectedReturn\": \"2023-08-07\"}")}))
    @PostMapping
    public ResponseEntity<Borrow> saveBorrow(
            @Valid @RequestBody @Parameter(description = "The borrow to be saved") Borrow borrow) {
        var savedBorrow = borrowService.addNewBorrow(borrow);
        return ResponseEntity
                .created(URI.create(String.format("/%d", borrow.getId())))
                .body(savedBorrow);
    }

    @Operation(summary = "Get a borrow by ID", description = "Retrieves a borrow by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"id\": 1, \"bookId\": 1, \"readerId\": 1, "
                            + "\"borrowStartDate\": \"2023-07-24\", \"borrowEndDate\": null, \"expectedReturn\": \"2023-08-07\"}")))
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<Borrow> getBorrowById(
            @PathVariable @Parameter(description = "The ID of the borrow") long id) {
        Optional<Borrow> borrow = borrowService.findByBorrowId(id);
        return borrow.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update borrow and return book", description = "Add date of returning for book to borrow")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"id\": 1, \"bookId\": 1, \"readerId\": 1, "
                            + "\"borrowStartDate\": \"2023-07-20\", \"borrowEndDate\": \"2023-07-25\", \"expectedReturn\": \"2023-07-29\"}")))
    @ApiResponse(responseCode = "404", description = "Not Found",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"dateTime\":\"2023-07-09T23:14:00.0304944\","
                            + "\"errorMessage\":\"An error occurred: This borrow id doesn't exist\"}")))
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBorrowReturnBook(
            @PathVariable @Parameter(description = "The ID of the borrow ") long id,
            @RequestBody @Parameter(description = "The updated borrow information") Borrow newBorrow) {
        borrowService.updateBorrowAndReturnBook(id);
        return ResponseEntity.ok("Borrow updated successfully, book is in library.");
    }

}
