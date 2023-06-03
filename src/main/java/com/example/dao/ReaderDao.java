package com.example.dao;

import com.example.entity.Book;
import com.example.entity.Reader;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReaderDao {
    List<Reader> findAll ();
    Optional<Reader> findById (long id);
    Reader save (Reader readerToSave);
    Optional<Reader> findByBookId(long bookId);
    Map<Reader, List<Book>> findAllWithBooks();
    void deleteById(long id);
}