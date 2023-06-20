package com.example.service;

import com.example.dao.ReaderDaoJdbcTemplateImpl;
import com.example.entity.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReaderService {

    @Autowired
    ReaderDaoJdbcTemplateImpl readerDaoJdbcTemplate;

    public List<Reader> findAllReaders () {
        return readerDaoJdbcTemplate.findAll();
    }

    public Reader addNewReader (Reader reader) {
        return readerDaoJdbcTemplate.save(reader);
    }

    public Optional<Reader> findByReaderId (long id) {
        return readerDaoJdbcTemplate.findById(id);
    }

    public Optional<Reader> getReaderByBookId (long bookId) {
        return readerDaoJdbcTemplate.findByBookId(bookId);
    }

    public void deleteReaderById (long id) {
        readerDaoJdbcTemplate.deleteById(id);
    }


}
