package com.example.dao;

import com.example.entity.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookDaoJdbcImplTest {

    private static BookDao bookDaoJdbcImpl;

    @BeforeAll
    public static void init(){
        bookDaoJdbcImpl = new BookDaoJdbcImpl();
    }

    @ParameterizedTest(name = "{index} ==> {2}")
    @DisplayName("Should throw DaoException when book name or/and author is null")
    @CsvSource(value = {",,name is null author is null",
            "name,, author is null",
            ",author, name is null"})
    void saveWithWrongParamsInBook(String name, String author, String description) {
        DAOException thrown = assertThrows(
                DAOException.class,
                () -> bookDaoJdbcImpl.save(new Book(name, author)),
                "Expected DAOException to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Database error during saving new book!" + "\nError details: "));
    }

    @Test
    @DisplayName("Should check book is created and appear in database")
    void saveBookAndCheckAppearsInDB() {
        var bookName = "Name";
        var bookAuthor = "Author";
        var bookToSave = new Book(bookName, bookAuthor);

        Optional<Book> existingInDb = bookDaoJdbcImpl.findAll().stream()
                .filter(book -> bookAuthor.equals(book.getAuthor()) && bookName.equals(book.getName()))
                .findAny();

        assertTrue(existingInDb.isEmpty(), "This book is not expected to be in DB before the test");

        var generatedId = bookDaoJdbcImpl.save(bookToSave).getId();
        assertTrue(generatedId > 0);

        var savedBook = bookDaoJdbcImpl.findById(generatedId).get();

        assertAll(
                () -> assertNotNull(savedBook),
                () -> assertEquals(bookName, savedBook.getName()),
                () -> assertEquals(bookAuthor, savedBook.getAuthor())
        );

        bookDaoJdbcImpl.deleteById(generatedId);
    }
}
