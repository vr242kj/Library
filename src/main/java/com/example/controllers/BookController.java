package com.example.controllers;

import com.example.entity.Book;
import com.example.entity.Reader;
import com.example.service.BookService;
import com.example.service.ReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/v1/books")
@Tag(name = "Book API", description = "Endpoints for managing books")
public class BookController {

    @Autowired
    private ReaderService readerService;

    @Autowired
    private BookService bookService;

    @Operation(summary = "Retrieve all books", description = "Retrieves all books")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "[{\"id\": 1, \"name\": \"Ulysses\","
                    + " \"author\": \"James Joyce\", \"readerId\": 0},"
                    + "{\"id\": 2, \"name\": \"Hamlet\", \"author\": \"William Shakespeare\", \"readerId\": 1}]")))
    @ApiResponse(responseCode = "204", description = "No Content", content = @Content)
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        var books = bookService.findAllBooks();

        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Save a book", description = "Saves a new book")
    @ApiResponse(responseCode = "201", description = "Created",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"id\": 2, \"name\": \"Hamlet\","
                    + " \"author\": \"William Shakespeare\", \"readerId\": 0}")))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"dateTime\":\"2023-07-09T23:14:00.0304944\","
                            + "\"errors\":[{\"fieldName\":\"author\", \"invalidValue\":\"987\"" +
                            ",\"constraint\":\"Author must be literal\"}]}")))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of the Item to be created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {@ExampleObject(value = "{\"name\": \"Hamlet\", \"author\": \"William Shakespeare\"}")}))
    @PostMapping
    public ResponseEntity<Book> saveBook(
            @Valid @RequestBody @Parameter(description = "The book to be saved") Book book) {
        var savedBook = bookService.addNewBook(book);
        return ResponseEntity
                .created(URI.create(String.format("/%d", book.getId())))
                .body(savedBook);
    }

    @Operation(summary = "Get a book by ID", description = "Retrieves a book by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"id\": 2, \"name\": \"Hamlet\","
                            + " \"author\": \"William Shakespeare\", \"readerId\": 1}")))
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
            @PathVariable @Parameter(description = "The ID of the book") long id) {
        Optional<Book> book = bookService.findByBookId(id);
        return book.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get reader by book ID", description = "Retrieves the reader of a book by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"id\":1, \"name\":\"Gabriel Garcia Marquez\"}")))
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @GetMapping("/{bookId}/readers")
    public ResponseEntity<Reader> getReaderByBookId(
            @PathVariable @Parameter(description = "The ID of the book") long bookId) {
        Optional<Reader> reader = readerService.getReaderByBookId(bookId);
        return reader.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update book availability", description = "Updates the availability of a book")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"id\": 0, \"name\": null,"
                            + " \"author\": null, \"readerId\": 1}")))
    @ApiResponse(responseCode = "404", description = "Not Found",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"dateTime\":\"2023-07-09T23:14:00.0304944\","
                            + "\"errorMessage\":\"An error occurred: Book with ID 1 does not exist. Failed to update\"}")))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of the Item to be created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {@ExampleObject(value = "{\"readerId\": 1}")}))
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBookAvailability(
            @RequestBody @Parameter(description = "The updated book information") Book newBook,
            @PathVariable  @Parameter(description = "The ID of the book") long id) {
        bookService.updateBook(newBook, id);
        return ResponseEntity.ok(newBook);
    }

    @Operation(summary = "Delete a book by ID", description = "Deletes a book by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "text/plain",
            examples = @ExampleObject(value = "Book deleted successfully.")))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"dateTime\":\"2023-07-09T23:14:00.0304944\","
                            + "\"errorMessage\":\"An error occurred: Book with ID 0 does not exist\"}")))
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(
            @PathVariable @Parameter(description = "The ID of the book") long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.ok("Book deleted successfully.");
    }

}
