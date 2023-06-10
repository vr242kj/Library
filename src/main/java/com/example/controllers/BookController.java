package com.example.controllers;

import com.example.dao.BookDao;
import com.example.entity.Book;
import com.example.entity.Reader;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    private BookDao bookDao;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        var books = bookDao.findAll();
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<Book> saveBook(@Valid @RequestBody Book book) {
        var bookToSave = bookDao.save(book);
        return ResponseEntity
                .created(URI.create(String.format("/book/%d", book.getId())))
                .body(bookToSave);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable long id) {
        Optional<Book> book = bookDao.findById(id);
        return book.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reader/{readerId}")
    public ResponseEntity<List<Book>> getBooksByReaderId(@PathVariable long readerId) {
        List<Book> books = bookDao.findAllByReaderId(readerId);
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{bookId}/borrow/{readerId}")
    public ResponseEntity<String> borrowBookToReader(@PathVariable long bookId, @PathVariable long readerId) {
        bookDao.borrowBookToReader(bookId, readerId);
        return ResponseEntity.ok("Book borrowed successfully.");
    }

    @PutMapping("/{bookId}/return")
    public ResponseEntity<String> returnBookToLibrary(@PathVariable long bookId) {
        bookDao.returnBookToLibrary(bookId);
        return ResponseEntity.ok("Book returned to the library.");
    }

    @GetMapping("/with-readers")
    public ResponseEntity<Map<Book, Optional<Reader>>> getAllBooksWithReaders() {
        Map<Book, Optional<Reader>> allBooksWithReaders = bookDao.findAllWithReaders();
        return ResponseEntity.ok(allBooksWithReaders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable long id) {
        bookDao.deleteById(id);
        return ResponseEntity.ok("Book deleted successfully.");
    }

}
