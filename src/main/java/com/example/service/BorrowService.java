package com.example.service;

import com.example.dao.BookDaoJdbcTemplateImpl;
import com.example.dao.BorrowDaoJdbcTemplateImpl;
import com.example.dao.ReaderDaoJdbcTemplateImpl;
import com.example.entity.Book;
import com.example.entity.Borrow;
import com.example.entity.Reader;
import com.example.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowDaoJdbcTemplateImpl borrowDaoJdbcTemplate;
    private final BookDaoJdbcTemplateImpl bookDaoJdbcTemplate;
    private final ReaderDaoJdbcTemplateImpl readerDaoJdbcTemplate;

    @Value("${library.maxBooksForBorrow}")
    private int maxBooksForBorrow;

    @Value("${library.minAgeForRestrictedBooks}")
    private int minAgeForRestrictedBooks;

    public List<Borrow> findAllBorrows() {
        return borrowDaoJdbcTemplate.findAll();
    }

    public Optional<Borrow> findByBorrowId(long id) {
        return borrowDaoJdbcTemplate.findById(id);
    }

    public Borrow addNewBorrow(Borrow borrow) {
        long readerId = borrow.getReaderId();
        long bookId = borrow.getBookId();

        Reader reader = getReaderByIdIfExist(readerId);
        Book book = getBookByIdIfExist(bookId);

        checkBorrowRules(reader, book);

        return borrowDaoJdbcTemplate.save(borrow);
    }

    private Reader getReaderByIdIfExist(long readerId) {
        return readerDaoJdbcTemplate.findById(readerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("This reader id %d doesn't exist", readerId)));
    }

    private Book getBookByIdIfExist(long bookId) {
        return bookDaoJdbcTemplate.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("This book id %d doesn't exist", bookId)));
    }

    private void checkBorrowRules(Reader reader, Book  book) {
        List<Borrow> allBorrows = borrowDaoJdbcTemplate.findAll();

        isBookAvailableForBorrow(allBorrows, book.getId());
        amountBorrowedBookByReaderId(allBorrows, reader.getId());
        expiredBorrow(allBorrows, reader.getId());
        accessibilityBookForReaderByAge(reader.getBirthdate(), book.isRestricted());
    }

    private void amountBorrowedBookByReaderId(List<Borrow> allBorrows, Long readerId) {
        long amountBorrowedBookByReaderId = allBorrows.stream()
                .filter(borrow -> borrow.getReaderId() == readerId)
                .filter(borrow -> borrow.getBorrowEndDate() == null)
                .count();

        if (amountBorrowedBookByReaderId >= maxBooksForBorrow) {
            throw new ServiceException("The reader has reached the limit of borrowed books");
        }
    }

    private void expiredBorrow(List<Borrow> allBorrows, Long readerId) {
        Optional<Borrow> expiredBorrow = allBorrows.stream()
                .filter(borrow -> borrow.getReaderId() == readerId)
                .filter(borrow -> borrow.getBorrowEndDate() == null)
                .filter(borrow -> borrow.getExpectedReturn().isBefore(LocalDate.now()))
                .findFirst();

        if (expiredBorrow.isPresent()) {
            throw new ServiceException("The reader exceeded the maximum borrowed time for the book.");
        }
    }

    private void accessibilityBookForReaderByAge(LocalDate readerBirthday, Boolean isBookRestricted) {
        long readerAge  = ChronoUnit.YEARS.between(readerBirthday, LocalDate.now());

        if (isBookRestricted && readerAge < minAgeForRestrictedBooks) {
            throw new ServiceException("Rejected! Reader isn't old enough to borrow the restricted book");
        }
    }

    private void isBookAvailableForBorrow(List<Borrow> allBorrows, Long bookId) {
        Optional<Borrow> isBookInLibrary = allBorrows.stream()
                .filter(borrow -> borrow.getBookId() == bookId)
                .filter(borrow -> borrow.getBorrowEndDate() == null)
                .findFirst();

        if (isBookInLibrary.isPresent()) {
            throw new ServiceException("Rejected! Book isn't available");
        }
    }

    public void updateBorrowAndReturnBook(long borrowId) {
        borrowDaoJdbcTemplate.findById(borrowId)
                .orElseThrow(() -> new ServiceException("This borrow id doesn't exist"));

        LocalDate currentDate = LocalDate.now();
        borrowDaoJdbcTemplate.returnBookToLibraryByBorrowId(borrowId, currentDate);
    }

    public Optional<Borrow> getBorrowByBookId(long bookId) {
        getBookByIdIfExist(bookId);

        return borrowDaoJdbcTemplate.findBorrowByBookId(bookId);
    }

    public List<Borrow> getAllBorrowsByReaderId(long readerId) {
        getReaderByIdIfExist(readerId);

        return borrowDaoJdbcTemplate.findAllBorrowsByReaderId(readerId);
    }

}
