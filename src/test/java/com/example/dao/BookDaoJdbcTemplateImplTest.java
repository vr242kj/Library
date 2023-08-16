package com.example.dao;

import com.example.entity.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
class BookDaoJdbcTemplateImplTest {
    private static BookDaoJdbcTemplateImpl bookDaoJdbcImpl;

    @BeforeAll
    public static void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        bookDaoJdbcImpl = new BookDaoJdbcTemplateImpl(jdbcTemplate);
    }

    @ParameterizedTest(name = "{index} ==> {2}")
    @DisplayName("Should throw DaoException when book name or/and author is null")
    @CsvSource(value = {",,false, name is null author is null",
            "name,,false, author is null",
            ",author,false, name is null"})
    void saveWithWrongParamsInBook(String name, String author,Boolean restricted, String description) {
        DAOException thrown = assertThrows(
                DAOException.class,
                () -> bookDaoJdbcImpl.save(new Book(name, author, restricted)),
                "Expected DAOException to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Failed to save new book"));
    }

    @Test
    @DisplayName("Should check book is created and appear in database")
    void saveBookAndCheckAppearsInDB() {
        var bookName = "Name";
        var bookAuthor = "Author";
        var restricted = false;
        var bookToSave = new Book(bookName, bookAuthor, restricted);

        Optional<Book> existingInDb = bookDaoJdbcImpl.findAll().stream()
                .filter(book -> bookAuthor.equals(book.getAuthor()) && bookName.equals(book.getName()))
                .findAny();

        assertTrue(existingInDb.isEmpty(), "This book is not expected to be in DB before the test");

        var generatedId = bookDaoJdbcImpl.save(bookToSave).getId();
        assertTrue(generatedId > 0);

        var savedBook = bookDaoJdbcImpl.findById(generatedId);

        assertAll(
                () -> assertNotNull(savedBook),
                () -> assertEquals(bookName, savedBook.map(Book::getName).orElse(null)),
                () -> assertEquals(bookAuthor, savedBook.map(Book::getAuthor).orElse(null))
        );

        bookDaoJdbcImpl.deleteById(generatedId);
    }
}
