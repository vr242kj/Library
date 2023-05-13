package com.example.controllers;

import com.example.dao.BookDao;
import com.example.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
public class BookController {

    @Autowired
    private BookDao bookDao;

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        var books = bookDao.findAll();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping("/books")
    public ResponseEntity<Book> saveBook(@RequestBody Book book) {
        var bookToSave = bookDao.save(book);
        return ResponseEntity
                .created(URI
                        .create(String.format("/book/%d", book.getId())))
                .body(bookToSave);
    }
}
