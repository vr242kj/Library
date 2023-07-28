package com.example.dao;

import com.example.entity.Reader;

import java.util.List;
import java.util.Optional;

public interface ReaderDao {
    List<Reader> findAll ();
    Optional<Reader> findById (long id);
    Reader save (Reader readerToSave);
    void deleteById(long id);
}
