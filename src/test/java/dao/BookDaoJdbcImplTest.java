package dao;

import entity.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ServiceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BookDaoJdbcImplTest {

    private static BookDao bookDaoJdbcImpl;

    @BeforeAll
    public static void init(){
        bookDaoJdbcImpl = new BookDaoJdbcImpl();
    }

    @Test
    @DisplayName("Should catch DAOException exception when book name is null")
    void saveWithNullNameInBook() {
        Book book = new Book(null,"XXX");
        assertThrows(DAOException.class,
                () -> bookDaoJdbcImpl.save(book));
    }

    @Test
    @DisplayName("Should catch DAOException exception when book author is null")
    void saveWithNullAuthorInBook() {
        Book book = new Book("XXX",null);
        assertThrows(DAOException.class,
                () -> bookDaoJdbcImpl.save(book));
    }

    @Test
    @DisplayName("Should catch DAOException exception when book name and author is null")
    void saveWithNullNameAndAuthorInBook() {
        Book book = new Book(null,null);
        assertThrows(DAOException.class,
                () -> bookDaoJdbcImpl.save(book));
    }

    @Test
    @DisplayName("Should create new book with unique id")
    void saveBookAndCreateUniqueId() {
        Book book = new Book("XXX","YYY");
        Book returnedBook = bookDaoJdbcImpl.save(book);
        assertTrue(returnedBook.getId() != 0);
    }

    @Test
    @DisplayName("Should check book is created and appear in database")
    void saveBookAndCheckAppearsInDB() {
        Book book = new Book("XXX","YYY");
        Book returnedBook = bookDaoJdbcImpl.save(book);
        assertTrue(returnedBook.getId() != 0);
        List<Book> listAllBooks= bookDaoJdbcImpl.findAll();
        //assertTrue(listAllBooks.contains(returnedBook));
        assertTrue(bookDaoJdbcImpl.findById(returnedBook.getId()).isPresent());
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAllByReaderId() {
    }
    @Test
    void borrowBookToReader() {
    }

    @Test
    void returnBookToLibrary() {
    }

    @Test
    void findAllWithReaders() {
    }
}