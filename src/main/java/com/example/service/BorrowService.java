package com.example.service;

import com.example.dao.BookDaoJdbcTemplateImpl;
import com.example.dao.BorrowDaoJdbcTemplateImpl;
import com.example.dao.ReaderDaoJdbcTemplateImpl;
import com.example.entity.Borrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Value( "${library.maxBooksForBorrow:4}" )
    private int maxBooksForBorrow;

    public List<Borrow> findAllBorrows() {
        return borrowDaoJdbcTemplate.findAll();
    }

    public Borrow addNewBorrow(Borrow borrow) {
        long readerId = borrow.getReaderId();
        long bookId = borrow.getBookId();

        readerDaoJdbcTemplate.findById(readerId)
                .orElseThrow(() -> new ServiceException("This reader id doesn't exist"));

        bookDaoJdbcTemplate.findById(bookId)
                .orElseThrow(() -> new ServiceException("This book id doesn't exist"));

        checkBorrowRules(readerId,bookId);

        return borrowDaoJdbcTemplate.save(borrow);
    }

    private void checkBorrowRules(long readerId, long bookId) {
        Optional<Integer> borrowCountByReaderId = borrowDaoJdbcTemplate.countAllByReaderId(readerId);

        if (borrowCountByReaderId.isPresent() && borrowCountByReaderId.get() >= maxBooksForBorrow) {
            throw new ServiceException("The reader has reached the limit of borrowed books");
        }

        Optional<Integer> countReaderExpiredBook = borrowDaoJdbcTemplate.isReaderExpiredMaximumBorrowedTime(readerId);

        if (countReaderExpiredBook.isPresent() && countReaderExpiredBook.get() > 0) {
            throw new ServiceException("The reader exceeded maximum borrowed time for the book");
        }

        Optional<Integer> countDifferenceReaderAgeAndMinAgeForBook = borrowDaoJdbcTemplate.isAdultToBorrowRestrictedBook(readerId);

        if (countDifferenceReaderAgeAndMinAgeForBook.isPresent() && countDifferenceReaderAgeAndMinAgeForBook.get() < 0) {
            throw new ServiceException("Rejected! Reader isn't old enough to borrow the restricted book");
        }

        Optional<Integer> isBookInLibrary = borrowDaoJdbcTemplate.isBookAvailable(bookId);

        if (isBookInLibrary.isPresent() && isBookInLibrary.get() > 0) {
            throw new ServiceException("Rejected! Book isn't available");
        }
    }

    public Optional<Borrow> findByBorrowId(long id) {
        return borrowDaoJdbcTemplate.findById(id);
    }

    public void deleteBorrowById(long id) {
        borrowDaoJdbcTemplate.deleteById(id);
    }

}
