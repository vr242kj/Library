package com.example.controllers;

import com.example.entity.Book;
import com.example.entity.Reader;
import com.example.service.BookService;
import com.example.service.ReaderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/readers")
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Reader>> getAllReaders() {
        List<Reader> readers = readerService.findAllReaders();

        if (readers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(readers);
    }

    @PostMapping
    public ResponseEntity<Reader> saveReader(@Valid @RequestBody Reader reader) {
        var readerToSave = readerService.addNewReader(reader);
        return ResponseEntity
                .created(URI.create(String.format("/reader/%d", reader.getId())))
                .body(readerToSave);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReaderById(@PathVariable long id) {
        Optional<Reader> reader = readerService.findByReaderId(id);
        return reader.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{readerId}/books")
    public ResponseEntity<List<Book>> getBooksByReaderId(@PathVariable long readerId) {
        List<Book> books = bookService.getBooksByReaderId(readerId);

        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReaderById(@PathVariable long id) {
        readerService.deleteReaderById(id);
        return ResponseEntity.ok("Reader deleted successfully.");
    }

}
