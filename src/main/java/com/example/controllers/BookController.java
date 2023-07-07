package com.example.controllers;

import com.example.entity.Book;
import com.example.entity.Reader;
import com.example.service.BookService;
import com.example.service.ReaderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/books")
public class BookController {

    @Autowired
    private ReaderService readerService;

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        var books = bookService.findAllBooks();

        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<Book> saveBook(@Valid @RequestBody Book book) {
        var savedBook = bookService.addNewBook(book);
        return ResponseEntity
                .created(URI.create(String.format("/%d", book.getId())))
                .body(savedBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable long id) {
        Optional<Book> book = bookService.findByBookId(id);
        return book.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{bookId}/readers")
    public ResponseEntity<Reader> getReaderByBookId(@PathVariable long bookId) {
        Optional<Reader> reader = readerService.getReaderByBookId(bookId);
        return reader.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBookAvailability(@RequestBody Book newBook, @PathVariable long id) {
        bookService.updateBook(newBook, id);
        return ResponseEntity.ok(newBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.ok("Book deleted successfully.");
    }

}
