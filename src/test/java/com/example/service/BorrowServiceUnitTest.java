package com.example.service;

import com.example.dao.BookDaoJdbcTemplateImpl;
import com.example.dao.BorrowDaoJdbcTemplateImpl;
import com.example.dao.ReaderDaoJdbcTemplateImpl;
import com.example.entity.Book;
import com.example.entity.Borrow;
import com.example.entity.Reader;
import com.example.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowServiceUnitTest {

    @InjectMocks
    private BorrowService borrowService;

    @Mock
    private BorrowDaoJdbcTemplateImpl borrowDaoJdbcTemplate;

    @Mock
    private BookDaoJdbcTemplateImpl bookDaoJdbcTemplate;

    @Mock
    private ReaderDaoJdbcTemplateImpl readerDaoJdbcTemplate;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(borrowService, "maxBooksForBorrow", 4);
        ReflectionTestUtils.setField(borrowService, "minAgeForRestrictedBooks", 18);
    }

    @Test
    void findAllBorrows_WhenBorrowsExist_ReturnListOfBorrows() {
        List<Borrow> actualBorrows = List.of(
                new Borrow(1, 1, 1, LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-10"), LocalDate.parse("2020-01-18")),
                new Borrow(2, 2, 2, LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-10"), LocalDate.parse("2020-01-18"))
        );

        when(borrowDaoJdbcTemplate.findAll()).thenReturn(actualBorrows);

        List<Borrow> expectedBorrows = borrowService.findAllBorrows();

        assertEquals(expectedBorrows, actualBorrows);
    }

    @Test
    void findByBorrowId_WhenBorrowExists_ReturnBorrow() {
        Borrow borrow =  new Borrow(1, 1, 1, LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-10"), LocalDate.parse("2020-01-18"));

        when(borrowDaoJdbcTemplate.findById(borrow.getId())).thenReturn(Optional.of(borrow));

        Optional<Borrow> expectedBorrow = borrowService.findByBorrowId(borrow.getId());

        assertEquals(borrow, expectedBorrow.get());
    }

    @Test
    void getBorrowByBookId_WhenBorrowExistsForBook_ReturnBorrow() {
        Borrow borrow =  new Borrow(1, 1, 1, LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-10"), LocalDate.parse("2020-01-18"));

        when(bookDaoJdbcTemplate.findById(borrow.getBookId())).thenReturn(Optional.of(new Book(1, "name", "author", 10, false)));

        when(borrowDaoJdbcTemplate.findBorrowByBookId(borrow.getBookId())).thenReturn(Optional.of(borrow));

        Optional<Borrow> expectedBorrow = borrowService.getBorrowByBookId(borrow.getId());

        assertEquals(borrow, expectedBorrow.get());
    }


    @Test
    void getAllBorrowsByReaderId_WhenBorrowsExistForReader_ReturnListOfBorrows() {
        Borrow borrow = new Borrow(1, 1, 1, LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-10"), LocalDate.parse("2020-01-18"));

        when(readerDaoJdbcTemplate.findById(borrow.getReaderId())).thenReturn(Optional.of(new Reader(1, "name", LocalDate.parse("1999-01-10"))));

        when(borrowDaoJdbcTemplate.findAllBorrowsByReaderId(borrow.getReaderId())).thenReturn(List.of(borrow));

        List<Borrow> expectedBorrow = borrowService.getAllBorrowsByReaderId(borrow.getId());

        assertEquals(borrow, expectedBorrow.get(0));
    }

    @Test
    void addNewBorrow_WhenReaderNotFound_ThrowResourceNotFoundException() {
        Borrow borrow = new Borrow(1, 99999);

        when(readerDaoJdbcTemplate.findById(borrow.getReaderId())).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> borrowService.addNewBorrow(borrow));
    }

    @Test
    void addNewBorrow_WhenBookNotFound_ThrowResourceNotFoundException() {
        Borrow borrow = new Borrow(99999, 1);

        lenient().when(bookDaoJdbcTemplate.findById(borrow.getBookId())).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> borrowService.addNewBorrow(borrow));
    }

    @Test
    void addNewBorrow_WhenBookNotAvailableForBorrow_ThrowServiceException() {
        Borrow borrow = new Borrow(1, 1);

        List<Borrow> actualBorrows = List.of(
                new Borrow(1, 1, 1, LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-18")),
                new Borrow(2, 2, 2, LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-18"))
        );

        when(readerDaoJdbcTemplate.findById(borrow.getReaderId())).thenReturn(Optional.of(new Reader(1, "name", LocalDate.parse("1999-01-10"))));
        when(bookDaoJdbcTemplate.findById(borrow.getBookId())).thenReturn(Optional.of(new Book(1, "name", "author", 10, false)));

        when(borrowDaoJdbcTemplate.findAll()).thenReturn(actualBorrows);

        Exception exception = assertThrows(ServiceException.class, () -> borrowService.addNewBorrow(borrow));

        String expectedMessage = "Rejected! Book isn't available";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void addNewBorrow_WhenReaderReachedBorrowLimit_ThrowServiceException() {
        Borrow borrow = new Borrow(6, 1);

        List<Borrow> actualBorrows = List.of(
                new Borrow(1, 1, 1, LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-18")),
                new Borrow(2, 2, 1, LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-18")),
                new Borrow(3, 3, 1, LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-18")),
                new Borrow(4, 4, 1, LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-18"))
        );

        when(readerDaoJdbcTemplate.findById(borrow.getReaderId())).thenReturn(Optional.of(new Reader(1, "name", LocalDate.parse("1999-01-10"))));
        when(bookDaoJdbcTemplate.findById(borrow.getBookId())).thenReturn(Optional.of(new Book(6, "name", "author", 10, false)));

        when(borrowDaoJdbcTemplate.findAll()).thenReturn(actualBorrows);

        Exception exception = assertThrows(ServiceException.class, () -> borrowService.addNewBorrow(borrow));

        String expectedMessage = "The reader has reached the limit of borrowed books";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void addNewBorrow_WhenReaderExceededMaximumBorrowedTime_ThrowServiceException() {
        Borrow borrow = new Borrow(2, 1);

        List<Borrow> actualBorrows = List.of(
                new Borrow(1, 1, 1, LocalDate.now().minusDays(2), LocalDate.now().minusDays(1))
                );

        when(readerDaoJdbcTemplate.findById(borrow.getReaderId())).thenReturn(Optional.of(new Reader(1, "name", LocalDate.parse("1999-01-10"))));
        when(bookDaoJdbcTemplate.findById(borrow.getBookId())).thenReturn(Optional.of(new Book(2, "name", "author", 10, false)));

        when(borrowDaoJdbcTemplate.findAll()).thenReturn(actualBorrows);

        Exception exception = assertThrows(ServiceException.class, () -> borrowService.addNewBorrow(borrow));

        String expectedMessage = "The reader exceeded the maximum borrowed time for the book.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void addNewBorrow_WhenReaderNotOldEnoughForRestrictedBook_ThrowServiceException() {
        Borrow borrow = new Borrow(1, 1);

        when(readerDaoJdbcTemplate.findById(borrow.getReaderId())).thenReturn(Optional.of(new Reader(1, "name", LocalDate.now().minusYears(15))));
        when(bookDaoJdbcTemplate.findById(borrow.getBookId())).thenReturn(Optional.of(new Book(1, "name", "author", 10, true)));

        Exception exception = assertThrows(ServiceException.class, () -> borrowService.addNewBorrow(borrow));

        String expectedMessage = "Rejected! Reader isn't old enough to borrow the restricted book";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
