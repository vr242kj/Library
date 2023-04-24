package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dao.BookDao;
import dao.DAOException;
import dao.ReaderDao;
import entity.Book;
import entity.Reader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {
    @Mock
    BookDao bookDao;
    @Mock
    ReaderDao readerDao;

    @InjectMocks
    LibraryService libraryService;

    @ParameterizedTest(name = "{index} ==> {1}: is {0}")
    @DisplayName("check validation, should catch ServiceException exception")
    @CsvSource(value = {"new String():empty string",
            "1:one number",
            "1/:empty reader id",
            "/1:empty book id",
            "1//1:two slashes",
            "1 /1:one space before slash",
            "1/ 1:one space after slash",
            "1/abc:non number reader id",
            "abc/1:non number book id"}, delimiter = ':')
    void borrowBookToReaderWithWrongInput(String input, String description) {
        ServiceException thrown = assertThrows(
                ServiceException.class,
                () -> libraryService.borrowBookToReader(input),
                "Expected ServiceException to throw, but it didn't"
        );
        assertEquals("Try again like this: book id/reader id. One slash, without spaces before and after. " +
                "Book id and reader id must be numeric", thrown.getMessage());
    }

    @Test
    @DisplayName("check existing reader id, should catch ServiceException exception")
    void borrowBookToReaderWithNonexistentReaderId() {
        when(bookDao.findById(1)).thenReturn(Optional.of(new Book(1,"X","Y")));
        when(readerDao.findById(999999)).thenReturn(Optional.empty());
        ServiceException thrown = assertThrows(
                ServiceException.class,
                () -> libraryService.borrowBookToReader("1/999999"),
                "Expected ServiceException to throw, but it didn't"
        );
        assertEquals("This reader id doesn't exist", thrown.getMessage());
    }

    @Test
    @DisplayName("check existing book id, should catch ServiceException exception")
    void borrowBookToReaderWithNonexistentBookId() {
        when(bookDao.findById(999999)).thenReturn(Optional.empty());
        ServiceException thrown = assertThrows(
                ServiceException.class,
                () -> libraryService.borrowBookToReader("999999/1"),
                "Expected ServiceException to throw, but it didn't"
        );
        assertEquals("This book id doesn't exist", thrown.getMessage());
    }

    @Test
    @DisplayName("Should call method borrowBookToReader in bookDao for adding book to reader")
    void borrowBookToReader() {
        when(bookDao.findById(1)).thenReturn(Optional.of(new Book(1,"X","Y")));
        when(readerDao.findById(1)).thenReturn(Optional.of(new Reader(1,"Z")));
        libraryService.borrowBookToReader("1/1");
        verify(bookDao).borrowBookToReader(1,1);
    }

    @ParameterizedTest(name = "{index} ==> {1}: is {0}")
    @CsvSource(value = {"1/1:number input, one slash",
            "\\u00201/1:spaces at the begining",
            "1/1\\u0020:spaces at the end"}, delimiter = ':')
    @DisplayName("check validation, correct input")
    void borrowBookToReaderWithCorrectInput(String input, String description) {
        when(bookDao.findById(1)).thenReturn(Optional.of(new Book(1,"X","Y")));
        when(readerDao.findById(1)).thenReturn(Optional.of(new Reader(1,"Z")));
        libraryService.borrowBookToReader("1/1");
        verify(bookDao).borrowBookToReader(1,1);
    }

    @Test
    void currentReaderOfBook() {
    }

    @Test
    void allBorrowedBookByReaderId() {
    }

    @Test
    void returnBook() {
    }

    @Test
    void findAllReadersWithBorrowedBooks() {
    }

    @Test
    void findAllBooksWithReaders() {
    }
}