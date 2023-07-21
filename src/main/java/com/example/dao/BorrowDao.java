package com.example.dao;

import com.example.entity.Borrow;

import java.util.List;
import java.util.Optional;

public interface BorrowDao {
    List<Borrow> findAll ();
    Optional<Borrow> findById (long id);
    Borrow save (Borrow borrowToSave);
    void deleteById(long id);

}
