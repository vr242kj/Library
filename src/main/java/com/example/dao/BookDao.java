package com.example.dao;

import com.example.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    List<Book> findAll ();
    Optional<Book> findById (long id);
    Book save (Book bookToSave);
    void deleteById(long id);

}
