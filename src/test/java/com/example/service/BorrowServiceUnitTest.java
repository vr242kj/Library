package com.example.service;

//import com.example.TestUtils;

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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    //private TestUtils testUtils;

    @BeforeEach
    void setUp() {
        borrowService.setMaxBooksForBorrow(2);
        borrowService.setMinAgeForRestrictedBooks(18);
        //testUtils = new TestUtils();
    }

    @Test
    void findAllBorrows_WhenBorrowsExist_ReturnListOfBorrows() {
        //List<Borrow> actualBorrows = testUtils.generateListOfBorrow();
        List<Borrow> actualBorrows = List.of(
                new Borrow(1, 1, 1, LocalDate.parse("2020-01-08"),
                        LocalDate.parse("2020-01-10"), LocalDate.parse("2020-01-18")),
                new Borrow(2, 2, 1, LocalDate.parse("2020-01-08"),
                        LocalDate.parse("2020-01-10"), LocalDate.parse("2020-01-18"))
        );
        when(borrowDaoJdbcTemplate.findAll()).thenReturn(actualBorrows);

        List<Borrow> expectedBorrows = borrowService.findAllBorrows();

        assertEquals(expectedBorrows, actualBorrows);
    }

    @Test
    void findByBorrowId_WhenBorrowExists_ReturnBorrow() {
        //Borrow borrow = testUtils.generateBorrow();
        Borrow borrow = new Borrow(1, 1, 1, LocalDate.parse("2020-01-08"),
                LocalDate.parse("2020-01-10"), LocalDate.parse("2020-01-18"));
        when(borrowDaoJdbcTemplate.findById(borrow.getId())).thenReturn(Optional.of(borrow));

        Optional<Borrow> expectedBorrow = borrowService.findByBorrowId(borrow.getId());

        assertEquals(borrow, expectedBorrow.get());
    }

    @Test
    void getBorrowByBookId_WhenBorrowExistsForBook_ReturnBorrow() {
        //Borrow borrow = testUtils.generateBorrow();
        Borrow borrow = new Borrow(1, 1, 1, LocalDate.parse("2020-01-08"),
                LocalDate.parse("2020-01-10"), LocalDate.parse("2020-01-18"));

        when(bookDaoJdbcTemplate.findById(borrow.getBookId()))
                .thenReturn(Optional.of(new Book(1, "name",
                        "author", 10, false)));

        when(borrowDaoJdbcTemplate.findBorrowByBookId(borrow.getBookId())).thenReturn(Optional.of(borrow));

        Optional<Borrow> expectedBorrow = borrowService.getBorrowByBookId(borrow.getId());

        assertEquals(borrow, expectedBorrow.get());
    }

    @Test
    void getAllBorrowsByReaderId_WhenBorrowsExistForReader_ReturnListOfBorrows() {
        //Borrow borrow1 = testUtils.generateListOfBorrow().get(0);
        //Borrow borrow2 = testUtils.generateListOfBorrow().get(1);
        Borrow borrow1 = new Borrow(1, 1, 1,
                LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-18"));
        Borrow borrow2 = new Borrow(2, 2, 1,
                LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-18"));

        when(readerDaoJdbcTemplate.findById(borrow1.getReaderId()))
                .thenReturn(Optional.of(new Reader(1, "name", LocalDate.parse("1999-01-10"))));

        when(borrowDaoJdbcTemplate.findAllBorrowsByReaderId(borrow1.getReaderId()))
                .thenReturn(List.of(borrow1, borrow2));

        List<Borrow> expectedBorrows = borrowService.getAllBorrowsByReaderId(borrow1.getReaderId());

        assertEquals(2, expectedBorrows.size());
        assertEquals(borrow1, expectedBorrows.get(0));
        assertEquals(borrow2, expectedBorrows.get(1));
    }

    @Test
    void addNewBorrow_WhenReaderNotFound_ThrowResourceNotFoundException() {
        Borrow borrow = new Borrow(1, 99999);
        String errorMessage = String.format("This reader id %d doesn't exist", borrow.getReaderId());

        when(readerDaoJdbcTemplate.findById(borrow.getReaderId()))
                .thenThrow(new ResourceNotFoundException(errorMessage));

        Throwable thrown = assertThrows(ResourceNotFoundException.class, () -> borrowService.addNewBorrow(borrow));
        assertEquals(errorMessage, thrown.getMessage());
    }

    @Test
    void addNewBorrow_WhenBookNotFound_ThrowResourceNotFoundException() {
        Borrow borrow = new Borrow(99999, 1);
        String errorMessage = String.format("This book id %d doesn't exist", borrow.getBookId());

        when(readerDaoJdbcTemplate.findById(borrow.getReaderId()))
                .thenReturn(Optional.of(new Reader(1, "name", LocalDate.parse("1999-01-10"))));

        when(bookDaoJdbcTemplate.findById(borrow.getBookId()))
                .thenThrow(new ResourceNotFoundException(errorMessage));

        Throwable thrown = assertThrows(ResourceNotFoundException.class, () -> borrowService.addNewBorrow(borrow));
        assertEquals(errorMessage, thrown.getMessage());
    }

    @Test
    void addNewBorrow_WhenBookNotAvailableForBorrow_ThrowServiceException() {
        Borrow borrow = new Borrow(1, 1);

        //List<Borrow> actualBorrows = testUtils.generateListOfBorrow();
        List<Borrow> actualBorrows = List.of(
                new Borrow(1, 1, 1, LocalDate.parse("2020-01-08"),
                        LocalDate.parse("2020-01-10"))
        );

        when(readerDaoJdbcTemplate.findById(borrow.getReaderId()))
                .thenReturn(Optional.of(new Reader(1, "name", LocalDate.parse("1999-01-10"))));

        when(bookDaoJdbcTemplate.findById(borrow.getBookId()))
                .thenReturn(Optional.of(new Book(1, "name",
                        "author", 10, false)));

        when(borrowDaoJdbcTemplate.findAll()).thenReturn(actualBorrows);

        Throwable thrown = assertThrows(ServiceException.class, () -> borrowService.addNewBorrow(borrow));
        assertEquals("Rejected! Book isn't available", thrown.getMessage());
    }

    @Test
    void addNewBorrow_WhenReaderReachedBorrowLimit_ThrowServiceException() {
        Borrow borrow = new Borrow(3, 1);

        //List<Borrow> actualBorrows = testUtils.generateListOfBorrow();
        List<Borrow> actualBorrows = List.of(
                new Borrow(1, 1, 1,
                        LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-18")),
                new Borrow(2, 2, 1,
                        LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-18"))
        );

        when(readerDaoJdbcTemplate.findById(borrow.getReaderId()))
                .thenReturn(Optional.of(new Reader(1, "name", LocalDate.parse("1999-01-10"))));

        when(bookDaoJdbcTemplate.findById(borrow.getBookId()))
                .thenReturn(Optional.of(new Book(3, "name", "author", 10, false)));

        when(borrowDaoJdbcTemplate.findAll()).thenReturn(actualBorrows);

        Throwable thrown = assertThrows(ServiceException.class, () -> borrowService.addNewBorrow(borrow));
        assertEquals("The reader has reached the limit of borrowed books", thrown.getMessage());
    }

    @Test
    void addNewBorrow_WhenReaderExceededMaximumBorrowedTime_ThrowServiceException() {
        Borrow borrow = new Borrow(2, 1);

        List<Borrow> actualBorrows = List.of(
                new Borrow(1, 1, 1,
                        LocalDate.now().minusDays(2), LocalDate.now().minusDays(1))
        );

        when(readerDaoJdbcTemplate.findById(borrow.getReaderId()))
                .thenReturn(Optional.of(new Reader(1, "name", LocalDate.parse("1999-01-10"))));
        when(bookDaoJdbcTemplate.findById(borrow.getBookId()))
                .thenReturn(Optional.of(new Book(2, "name",
                        "author", 10, false)));

        when(borrowDaoJdbcTemplate.findAll()).thenReturn(actualBorrows);

        Throwable thrown = assertThrows(ServiceException.class, () -> borrowService.addNewBorrow(borrow));
        assertEquals("The reader exceeded the maximum borrowed time for the book.", thrown.getMessage());

    }

    @Test
    void addNewBorrow_WhenReaderNotOldEnoughForRestrictedBook_ThrowServiceException() {
        Borrow borrow = new Borrow(1, 1);

        when(readerDaoJdbcTemplate.findById(borrow.getReaderId()))
                .thenReturn(Optional.of(new Reader(1, "name",
                        LocalDate.now().minusYears(15))));
        when(bookDaoJdbcTemplate.findById(borrow.getBookId()))
                .thenReturn(Optional.of(new Book(1, "name",
                        "author", 10, true)));

        Throwable thrown = assertThrows(ServiceException.class, () -> borrowService.addNewBorrow(borrow));
        assertEquals("Rejected! Reader isn't old enough to borrow the restricted book", thrown.getMessage());
    }

    @Test
    void addNewBorrow_WhenBorrowParamsIsValid_ThanShouldReturnBorrow() {
        Borrow borrow = new Borrow(1, 1);

        when(bookDaoJdbcTemplate.findById(borrow.getBookId()))
                .thenReturn(Optional.of(new Book(1, "name",
                        "author", 10, false)));

        when(readerDaoJdbcTemplate.findById(borrow.getReaderId()))
                .thenReturn(Optional.of(new Reader(1, "name", LocalDate.parse("1999-01-10"))));

        when(borrowDaoJdbcTemplate.save(borrow)).thenReturn(borrow);

        var savedBorrow = borrowService.addNewBorrow(borrow);

        assertAll(
                () -> assertNotNull(savedBorrow),
                () -> assertEquals(borrow.getBookId(), savedBorrow.getBookId()),
                () -> assertEquals(borrow.getReaderId(), savedBorrow.getReaderId())
        );
    }

}
