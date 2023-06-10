package com.example.controllers;

import com.example.dao.ReaderDao;
import com.example.entity.Book;
import com.example.entity.Reader;
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
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/readers")
public class ReaderController {

    @Autowired
    private ReaderDao readerDao;

    @GetMapping
    public ResponseEntity<List<Reader>> getAllReaders() {
        List<Reader> readers = readerDao.findAll();
        return ResponseEntity.ok(readers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReaderById(@PathVariable long id) {
        Optional<Reader> reader = readerDao.findById(id);
        return reader.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Reader> saveReader(@Valid @RequestBody Reader reader) {
        var readerToSave = readerDao.save(reader);
        return ResponseEntity
                .created(URI.create(String.format("/reader/%d", reader.getId())))
                .body(readerToSave);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Reader> getReaderByBookId(@PathVariable long bookId) {
        Optional<Reader> reader = readerDao.findByBookId(bookId);
        return reader.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/with-books")
    public ResponseEntity<Map<Reader, List<Book>>> getAllReadersWithBooks() {
        Map<Reader, List<Book>> readersWithBooks = readerDao.findAllWithBooks();
        return ResponseEntity.ok(readersWithBooks);
    }

    @DeleteMapping("/{readerId}")
    public ResponseEntity<String> deleteReaderById(@PathVariable long readerId) {
        readerDao.deleteById(readerId);
        return ResponseEntity.ok("Reader deleted successfully.");
    }

}
