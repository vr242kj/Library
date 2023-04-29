package dao;

import entity.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("Should create new book with unique id")
    void saveBookAndCreateUniqueId() {
        Book book = new Book("XXX","YYY");
        Book returnedBook = bookDaoJdbcImpl.save(book);
        assertTrue(returnedBook.getId() != 0);
    }

    @Test
    @DisplayName("Should check book is created and appear in database")
    void saveBookAndCheckAppearsInDB() {
        Book exampleBook = new Book("name","author");
        List<Book> listAllBooksOnlyNameAndAuthor = bookDaoJdbcImpl.findAll().stream()
                .map(book -> new Book(book.getName(), book.getAuthor()))
                .toList();

        assertFalse(listAllBooksOnlyNameAndAuthor.contains(exampleBook));

        Book returnedBook = bookDaoJdbcImpl.save(exampleBook);
        assertTrue(returnedBook.getId() != 0);
        assertTrue(bookDaoJdbcImpl.findById(returnedBook.getId()).isPresent());

        assertEquals(bookDaoJdbcImpl.findById(returnedBook.getId()).get(), exampleBook);
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