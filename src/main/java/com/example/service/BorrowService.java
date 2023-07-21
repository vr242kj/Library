package com.example.service;

import com.example.dao.BookDaoJdbcTemplateImpl;
import com.example.dao.BorrowDaoJdbcTemplateImpl;
import com.example.dao.ReaderDaoJdbcTemplateImpl;
import com.example.entity.Book;
import com.example.entity.Borrow;
import com.example.entity.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowService {

    @Autowired
    BorrowDaoJdbcTemplateImpl borrowDaoJdbcTemplate;

    @Autowired
    BookDaoJdbcTemplateImpl bookDaoJdbcTemplate;

    @Autowired
    ReaderDaoJdbcTemplateImpl readerDaoJdbcTemplate;

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

        Reader reader = readerDaoJdbcTemplate.findById(readerId)
                .orElseThrow(() -> new ServiceException("This reader id doesn't exist"));

        Book book = bookDaoJdbcTemplate.findById(bookId)
                .orElseThrow(() -> new ServiceException("This book id doesn't exist"));

        checkBorrowRules(reader, book, readerId, bookId);

        return borrowDaoJdbcTemplate.save(borrow);
    }

    private void checkBorrowRules(Reader reader, Book  book, long readerId, long bookId) {
        List<Borrow> allBorrows = borrowDaoJdbcTemplate.findAll();

        amountBorrowedBookByReaderId(allBorrows, readerId);
        expiredBorrow(allBorrows, readerId);
        accessibilityBookForReaderByAge(reader.getBirthdate(), book.isRestricted());
        isBookInLibrary(allBorrows, bookId);
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

    private void isBookInLibrary(List<Borrow> allBorrows, Long bookId) {
        Optional<Borrow> isBookInLibrary = allBorrows.stream()
                .filter(borrow -> borrow.getBookId() == bookId)
                .filter(borrow -> borrow.getBorrowEndDate() == null)
                .findFirst();

        if (isBookInLibrary.isPresent()) {
            throw new ServiceException("Rejected! Book isn't available");
        }
    }

    public void deleteBorrowById(long id) {
        borrowDaoJdbcTemplate.deleteById(id);
    }

}
