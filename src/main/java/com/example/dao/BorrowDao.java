package com.example.dao;

import com.example.entity.Borrow;

import java.util.List;
import java.util.Optional;

public interface BorrowDao {
    List<Borrow> findAll ();
    Optional<Borrow> findById (long id);
    Borrow save (Borrow borrowToSave);
    Optional<Integer> countAllByReaderId(long readerId);
    Optional<Integer> isReaderExpiredMaximumBorrowedTime(long readerId);
    Optional<Integer> isAdultToBorrowRestrictedBook(long readerId);
    Optional<Integer> isBookAvailable(long bookId);
    void deleteById(long id);

}
