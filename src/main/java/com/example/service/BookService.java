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

    @Autowired
    ReaderService readerService;

    public List<Book> findAllBooks () {
        return bookDaoJdbcTemplate.findAll();
    }

    public Book addNewBook (Book book) {
        return bookDaoJdbcTemplate.save(book);
    }

    public Optional<Book> findByBookId (long id) {
        return bookDaoJdbcTemplate.findById(id);
    }

    public void borrowBookToReader (long bookId, long readerId) {
        bookDaoJdbcTemplate.borrowBookToReader(bookId, readerId);
    }

    public void returnBook (long bookId) {
        bookDaoJdbcTemplate.returnBookToLibrary(bookId);
    }

    public void updateBook (Book newBook, long bookId) {
        bookDaoJdbcTemplate.findById(bookId)
                .orElseThrow(() -> new ServiceException(String.format("Book with ID %d does not exist. Failed to update", bookId)));

        long readerId = newBook.getReaderId();

        if (readerId == 0) {
            returnBook(bookId);
            return;
        }

        readerService.findByReaderId(readerId)
                .orElseThrow(() -> new ServiceException(String.format("Reader with ID %d does not exist. "
                        + "Failed to borrow the book ID %d", readerId, bookId)));;

        borrowBookToReader(bookId, readerId);
    }

    public List<Book> getBooksByReaderId (long readerId) {
        return bookDaoJdbcTemplate.findAllByReaderId(readerId);
    }

    public void deleteBookById (long id) {
        bookDaoJdbcTemplate.deleteById(id);
    }

}
