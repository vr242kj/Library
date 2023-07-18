package com.example.service;

import com.example.dao.BookDaoJdbcTemplateImpl;
import com.example.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookDaoJdbcTemplateImpl bookDaoJdbcTemplate;

    public List<Book> findAllBooks() {
        return bookDaoJdbcTemplate.findAll();
    }

    public Book addNewBook(Book book) {
        return bookDaoJdbcTemplate.save(book);
    }

    public Optional<Book> findByBookId(long id) {
        return bookDaoJdbcTemplate.findById(id);
    }

    public void deleteBookById(long id) {
        bookDaoJdbcTemplate.deleteById(id);
    }

}
