package com.example.service;

import com.example.dao.BookDaoJdbcTemplateImpl;
import com.example.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookDaoJdbcTemplateImpl bookDaoJdbcTemplate;

    @Value("${library.defaultMaxBorrowTimeInDays}")
    private int defaultMaxBorrowTimeInDays;

    public List<Book> findAllBooks() {
        return bookDaoJdbcTemplate.findAll();
    }

    public Book addNewBook(Book book) {
        if (book.getMaxBorrowTimeInDay() == 0){
            book.setMaxBorrowTimeInDay(defaultMaxBorrowTimeInDays);
        }

        return bookDaoJdbcTemplate.save(book);
    }

    public Optional<Book> findByBookId(long id) {
        return bookDaoJdbcTemplate.findById(id);
    }

    public void deleteBookById(long id) {
        bookDaoJdbcTemplate.deleteById(id);
    }

}
