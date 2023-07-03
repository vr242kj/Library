package com.example.service;

import com.example.dao.BookDao;
import com.example.dao.DAOException;
import com.example.dao.ReaderDao;
import com.example.entity.Book;
import com.example.entity.Reader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {
    @Mock
    BookDao bookDao;
    @Mock
    ReaderDao readerDao;

    @InjectMocks
    LibraryService libraryService;

    @ParameterizedTest(name = "{index} ==> {1}: is {0}")
    @DisplayName("Should throw ServiceException when input doesnt match regex")
    @CsvSource(value = {"new String(),empty string",
            "1,one number",
            "1/,empty reader id",
            "/1,empty book id",
            "1//1,two slashes",
            "1 /1,one space before slash",
            "1/ 1,one space after slash",
            "1/abc,non number reader id",
            "abc/1,non number book id"})
    void borrowBookToReaderWithWrongInput(String input, String description) {
        ServiceException thrown = assertThrows(
                ServiceException.class,
                () -> libraryService.borrowBookToReader(input),
                "Expected ServiceException to throw, but it didn't"
        );
        assertEquals("Try again like this: book id/reader id. One slash, without spaces before and after. " +
                "Book id and reader id must be numeric", thrown.getMessage());
    }

    @ParameterizedTest(name = "{index} ==> {1} is {0}")
    @DisplayName("Should throw ServiceException when book id or reader id doesnt exist")
    @CsvSource(value = {"999999/1, book, book id not exist",
            "1/999999, reader, reader id not exist"})
    void borrowBookToReaderWithNonexistentReaderIdOrBookId(String input, String entity, String description) {
        lenient().when(bookDao.findById(1)).thenReturn(Optional.of(new Book(1,"X","Y")));
        ServiceException thrown = assertThrows(
                ServiceException.class,
                () -> libraryService.borrowBookToReader(input),
                "Expected ServiceException to throw, but it didn't"
        );
        assertEquals("This " + entity + " id doesn't exist", thrown.getMessage());
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
    @CsvSource(value = {"1/1,number input, one slash",
            "' 1/1',spaces at the beginning",
            "'1/1 ',spaces at the end"})
    @DisplayName("Should call method borrowBookToReader in bookDao when input match regex")
    void borrowBookToReaderWithCorrectInput(String input, String description) {
        when(bookDao.findById(1)).thenReturn(Optional.of(new Book(1,"X","Y")));
        when(readerDao.findById(1)).thenReturn(Optional.of(new Reader(1,"Z")));
        libraryService.borrowBookToReader(input);
        verify(bookDao).borrowBookToReader(1,1);
    }

    @Test
    @DisplayName("Should return a list of all books")
    void findAllBooks() {
        List<Book> books = List.of(new Book(1, "X", "X"),
                new Book(2, "Y", "Y"),
                new Book(3, "Z", "Z"));

        when(bookDao.findAll()).thenReturn(books);
        List<Book> returnedBooks = libraryService.findAllBooks();
        assertEquals(books, returnedBooks);
    }

    @Test
    @DisplayName("Should return an empty list of all books")
    void findAllBooksWithEmptyList() {
        when(bookDao.findAll()).thenReturn(Collections.EMPTY_LIST);
        List<Book> returnedBooks = libraryService.findAllBooks();
        assertTrue(returnedBooks.isEmpty());
    }

    @Test
    @DisplayName("Should throw DAOException exception")
    void findAllBooksWithDaoException()  {
        when(bookDao.findAll()).thenThrow(DAOException.class);
        assertThrows(DAOException.class,
                () -> libraryService.findAllBooks());
    }

    @Test
    @DisplayName("Should return a list of all readers")
    void findAllReaders() {
        List<Reader> readers = List.of(new Reader(1, "Mike"),
                new Reader(2, "Jack"),
                new Reader(3, "Cake"));

        when(readerDao.findAll()).thenReturn(readers);
        List<Reader> returnedReaders = libraryService.findAllReaders();
        assertEquals(readers, returnedReaders);
    }

    @Test
    @DisplayName("Should return an empty list of all readers")
    void findAllReadersWithEmptyList() {
        when(readerDao.findAll()).thenReturn(Collections.EMPTY_LIST);
        List<Reader> returnedReaders = libraryService.findAllReaders();
        assertTrue(returnedReaders.isEmpty());
    }

    @Test
    @DisplayName("Should throw DAOException exception")
    void findAllReadersWithDaoException() {
        when(readerDao.findAll()).thenThrow(DAOException.class);
        assertThrows(DAOException.class,
                () -> libraryService.findAllReaders());
    }

    @ParameterizedTest(name = "{index} ==> {1}: is {0}")
    @DisplayName("Should throw ServiceException when input doesnt match regex")
    @CsvSource(value = {"'', empty string",
            "A, one letter",
            "!@#$%^&*()1234567890-/, non literal string",
            "one two three four, more than 3 words",
            "one  two, more than one space between words",
            "' 'one, one space at the beginning",
            "one two three' ', one space at the end"})
    void addNewReaderExceptionWrongInput(String input, String description) {
        ServiceException thrown = assertThrows(
                ServiceException.class,
                () -> libraryService.addNewReader(input),
                "Expected ServiceException to throw, but it didn't"
        );
        assertEquals("Full name must be literal and one space between words (max 3 words)", thrown.getMessage());
    }

    @Test
    @DisplayName("Should call method save in readerDao for creating new reader")
    void addNewReader() {
        Reader reader = new Reader("fullName");
        libraryService.addNewReader("fullName");
        verify(readerDao).save(reader);
    }

    @Test
    @DisplayName("Should throw DaoException")
    void addNewReaderDaoExceptionDatabaseError() {
        when(readerDao.save(new Reader("fullName"))).thenThrow(DAOException.class);
        assertThrows(DAOException.class,
                () -> libraryService.addNewReader("fullName"));
    }

    @ParameterizedTest(name = "{index} ==> {1}: is {0}")
    @DisplayName("Should throw ServiceException when input doesnt match regex")
    @CsvSource(value = {"' ', empty string",
            "A, one letter",
            "A/A, one letter for name and author",
            "AA/A, one letter for author",
            "AA/, empty author",
            "/AA, empty name",
            "AA//AA, two slashes",
            "AA' '/AA, one space before slash",
            "AA/' 'AA ,one space after slash",
            "AA/!@#$%^&*()1234567890-/, non literal author's name"})
    void addNewBookExceptionWrongInput(String input, String description) {
        ServiceException thrown = assertThrows(
                ServiceException.class,
                () -> libraryService.addNewBook(input),
                "Expected ServiceException to throw, but it didn't"
        );
        assertEquals("Try again like this: name/author. One slash, without spaces before and after. Author must be literal", thrown.getMessage());
    }

    @Test
    @DisplayName("Should call method save in bookDao for creating new book")
    void addNewBook() {
        Book book = new Book("name", "author");
        libraryService.addNewBook("name/author");
        verify(bookDao).save(book);
    }

    @Test
    @DisplayName("Should throw DAOException")
    void addNewBookDaoException() {
        when(bookDao.save(new Book("name", "author"))).thenThrow(DAOException.class);
        assertThrows(DAOException.class,
                () -> libraryService.addNewBook("name/author"));
    }

    @Test
    @DisplayName("Should call method returnBookToLibrary in bookDao")
    void returnBook() {
        when(bookDao.findById(1)).thenReturn(Optional.of(new Book(1,"XX","YY")));
        libraryService.returnBook("1");
        verify(bookDao).returnBookToLibrary(1);
    }

    @Test
    @DisplayName("Should throw ServiceException when book id not exist")
    void returnBookServiceExceptionWhenBookIdNotExist() {
        lenient().when(bookDao.findById(1)).thenReturn(Optional.empty());
        assertThrows(ServiceException.class,
                () -> libraryService.addNewBook("1"));
    }

    @Test
    @DisplayName("Should throw ServiceException when reader id not exist")
    void allBorrowedBookByReaderIdServiceExceptionWhenReaderIdNotExist() {
        when(readerDao.findById(1)).thenReturn(Optional.empty());
        assertThrows(ServiceException.class,
                () -> libraryService.allBorrowedBookByReaderId("1"));
    }

    @Test
    @DisplayName("Should return list of books by reader id")
    void allBorrowedBookByReaderId() {
        List<Book> books = List.of(new Book(1, "X", "X", 1),
                new Book(2, "Y", "Y", 1));
        when(readerDao.findById(1)).thenReturn(Optional.of(new Reader(1, "fullName")));
        when(bookDao.findAllByReaderId(1)).thenReturn(List.of(new Book(1, "X", "X", 1),
                new Book(2, "Y", "Y", 1)));
        List<Book> returnedBooks = libraryService.allBorrowedBookByReaderId("1");
        assertEquals(books, returnedBooks);
    }

    @Test
    @DisplayName("Should return empty list")
    void allBorrowedBookByReaderIdEmptyList() {
        when(readerDao.findById(1)).thenReturn(Optional.of(new Reader(1, "fullName")));
        when(bookDao.findAllByReaderId(1)).thenReturn(Collections.EMPTY_LIST);
        List<Book> returnedBooks = libraryService.allBorrowedBookByReaderId("1");
        assertTrue(returnedBooks.isEmpty());
    }

    @Test
    @DisplayName("Should throw ServiceException when reader id not exist")
    void currentReaderOfBookServiceExceptionWhenBookIdNotExist() {
        when(bookDao.findById(1)).thenReturn(Optional.empty());
        assertThrows(ServiceException.class,
                () -> libraryService.currentReaderOfBook("1"));
    }

    @Test
    @DisplayName("Should pass, when book hasn't reader")
    void currentReaderOfBookWhenBookInLibrary() {
        when(bookDao.findById(1)).thenReturn(Optional.of(new Book(1,"name", "author")));
        Optional<Reader> returnedReader = libraryService.currentReaderOfBook("1");
        assertTrue(returnedReader.isEmpty());
    }

    @Test
    @DisplayName("Should return reader of book")
    void currentReaderOfBook() {
        Optional<Reader> optionalReader = Optional.of(new Reader(1,"fullName"));
        when(bookDao.findById(1)).thenReturn(Optional.of(new Book(1,"name", "author", 1)));
        when(readerDao.findByBookId(1)).thenReturn(Optional.of(new Reader(1,"fullName")));
        Optional<Reader> returnedReader = libraryService.currentReaderOfBook("1");
        assertEquals(returnedReader, optionalReader);
    }

    @Test
    @DisplayName("Should return a map of all readers with borrowed books")
    void findAllReadersWithBorrowedBooks() {
        Map<Reader, List<Book>> mapReadersWithBooks = new TreeMap<>(Comparator.comparing(Reader::getId));
        mapReadersWithBooks.put(new Reader(1, "AAA"), List.of(new Book("XXX", "XXX")));
        mapReadersWithBooks.put(new Reader(2, "BBB"), List.of(new Book("YYY", "YYY")));
        when(readerDao.findAllWithBooks()).thenReturn(Map.of(new Reader(1, "AAA"), List.of(new Book("XXX", "XXX")),
                new Reader(2, "BBB"), List.of(new Book("YYY", "YYY"))));
        Map<Reader, List<Book>> returnedMapReadersWithBooks = libraryService.findAllReadersWithBorrowedBooks();
        assertEquals(mapReadersWithBooks, returnedMapReadersWithBooks);
    }

    @Test
    @DisplayName("Should return an empty map of all readers with borrowed books")
    void findAllReadersWithBorrowedBooksWithEmptyMap() {
        when(readerDao.findAllWithBooks()).thenReturn(Collections.EMPTY_MAP);
        Map<Reader, List<Book>> returnedMapReadersWithBooks = libraryService.findAllReadersWithBorrowedBooks();
        assertTrue(returnedMapReadersWithBooks.isEmpty());
    }

    @Test
    @DisplayName("Should throw DAOException exception")
    void findAllReadersWithBorrowedBooksWithDAOException() {
        when(readerDao.findAllWithBooks()).thenThrow(DAOException.class);
        assertThrows(DAOException.class,
                () -> libraryService.findAllReadersWithBorrowedBooks());
    }

    @Test
    @DisplayName("Should return a map of all books with reader")
    void findAllBooksWithReaders() {
        Map<Book, Optional<Reader>> mapBooksWithReaders = new TreeMap<>(Comparator.comparing(Book::getId));
        mapBooksWithReaders.put(new Book(1, "AAA", "AAA", 1), Optional.of(new Reader("XXX")));
        mapBooksWithReaders.put(new Book(2, "BBB", "BBB", 2), Optional.of(new Reader("YYY")));
        when(bookDao.findAllWithReaders()).thenReturn(Map.of(new Book(1, "AAA", "AAA", 1), Optional.of(new Reader("XXX")),
                new Book(2, "BBB", "BBB", 2), Optional.of(new Reader("YYY"))));
        Map<Book, Optional<Reader>> returnedMapBooksWithReaders = libraryService.findAllBooksWithReaders();
        assertEquals(mapBooksWithReaders, returnedMapBooksWithReaders);
    }

    @Test
    @DisplayName("Should return an empty map of all books with reader")
    void findAllBooksWithReadersWithEmptyMap() {
        when(bookDao.findAllWithReaders()).thenReturn(Collections.EMPTY_MAP);
        Map<Book, Optional<Reader>> returnedMapBooksWithReaders = libraryService.findAllBooksWithReaders();
        assertTrue(returnedMapBooksWithReaders.isEmpty());
    }

    @Test
    @DisplayName("Should throw DAOException exception")
    void findAllBooksWithReadersWithDAOException() {
        when(bookDao.findAllWithReaders()).thenThrow(DAOException.class);
        assertThrows(DAOException.class,
                () -> libraryService.findAllBooksWithReaders());
    }
}
