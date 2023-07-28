package com.example.service;

import com.example.dao.ReaderDaoJdbcTemplateImpl;
import com.example.entity.Reader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderService {
    private final ReaderDaoJdbcTemplateImpl readerDaoJdbcTemplate;

    public List<Reader> findAllReaders() {
        return readerDaoJdbcTemplate.findAll();
    }

    public Reader addNewReader(Reader reader) {
        return readerDaoJdbcTemplate.save(reader);
    }

    public Optional<Reader> findByReaderId(long id) {
        return readerDaoJdbcTemplate.findById(id);
    }

    public void deleteReaderById(long id) {
        readerDaoJdbcTemplate.deleteById(id);
    }

}
