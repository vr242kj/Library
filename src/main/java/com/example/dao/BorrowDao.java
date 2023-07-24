package com.example.dao;

import com.example.entity.Borrow;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BorrowDao {
    List<Borrow> findAll ();
    Optional<Borrow> findById (long id);
    Borrow save (Borrow borrowToSave);
    void returnBookToLibraryByBorrowId(long id, LocalDate currentDate);
    Optional<Borrow> findBorrowByBookId(long bookId);
    List<Borrow> findAllBorrowsByReaderId(long readerId);

}
