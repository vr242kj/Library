package com.example.controllers;

import com.example.entity.Borrow;
import com.example.entity.Reader;
import com.example.service.BorrowService;
import com.example.service.ReaderService;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/readers")
@Tag(name = "Reader API", description = "Endpoints for managing readers")
public class ReaderController {
    private final ReaderService readerService;
    private final BorrowService borrowService;

    @Operation(summary = "Retrieve all readers", description = "Retrieves all readers")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "[{\"id\": 1, \"name\": \"John\"},"
                    + "{\"id\": 2, \"name\": \"Ivan\"}]")))
    @ApiResponse(responseCode = "204", description = "No Content", content = @Content)
    @GetMapping
    public ResponseEntity<List<Reader>> getAllReaders() {
        var readers = readerService.findAllReaders();

        if (readers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(readers);
    }

    @Operation(summary = "Save a reader", description = "Saves a new reader")
    @ApiResponse(responseCode = "201", description = "Created",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"John\"}")))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"dateTime\":\"2023-07-09T23:14:00.0304944\","
                            + "\"errors\":[{\"fieldName\":\"name\", \"invalidValue\":\"8765\"" +
                            ",\"constraint\":\"Full name must be literal and one space between words (max 3 words)\"}]}")))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of the Item to be created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {@ExampleObject(value = "{\"name\": \"John\"}")}))
    @PostMapping
    public ResponseEntity<Reader> saveReader(@Valid @RequestBody Reader reader) {
        var savedReader = readerService.addNewReader(reader);
        return ResponseEntity
                .created(URI.create(String.format("/reader/%d", reader.getId())))
                .body(savedReader);
    }

    @Operation(summary = "Get a reader by ID", description = "Retrieves a reader by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"John\"}")))
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReaderById(
            @PathVariable @Parameter(description = "The ID of the reader") long id) {
        Optional<Reader> reader = readerService.findByReaderId(id);
        return reader.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a reader by ID", description = "Deletes a reader by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "text/plain",
            examples = @ExampleObject(value = "Reader deleted successfully.")))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"dateTime\":\"2023-07-09T23:14:00.0304944\","
                    + "\"errorMessage\":\"An error occurred: Reader with ID 0 does not exist\"}")))
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReaderById(
            @PathVariable @Parameter(description = "The ID of the reader") long id) {
        readerService.deleteReaderById(id);
        return ResponseEntity.ok("Reader deleted successfully.");
    }

    @Operation(summary = "Retrieve all borrows by reader id", description = "Retrieves all borrows by reader id where the book is borrowed")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "[{\"id\": 1, \"bookId\": 1, \"readerId\": 1,"
                            + "\"borrowStartDate\": \"2023-07-24\", \"borrowEndDate\": null, \"expectedReturn\": \"2023-08-07\"},"
                            + "{\"id\": 2, \"bookId\": 2, \"readerId\": 1, "
                            + "\"borrowStartDate\": \"2023-07-20\", \"borrowEndDate\": null, \"expectedReturn\": \"2023-08-07\"}]")))
    @ApiResponse(responseCode = "204", description = "No Content", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"dateTime\":\"2023-07-09T23:14:00.0304944\","
                            + "\"errorMessage\":\"An error occurred: Reader with ID 0 does not exist\"}")))
    @GetMapping("/{readerId}/borrows")
    public ResponseEntity<List<Borrow>>  getAllBorrowsByReaderId(@PathVariable long readerId) {
        List<Borrow> borrows = borrowService.getAllBorrowsByReaderId(readerId);

        if (borrows.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(borrows);
    }

}
