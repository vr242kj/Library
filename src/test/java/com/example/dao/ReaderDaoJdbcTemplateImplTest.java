package com.example.dao;

import com.example.entity.Reader;
import com.example.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;

@JdbcTest
@AutoConfigureTestDatabase
class ReaderDaoJdbcTemplateImplTest {

    private static ReaderDaoJdbcTemplateImpl readerDaoJdbcTemplate;

    @BeforeAll
    public static void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        readerDaoJdbcTemplate = new ReaderDaoJdbcTemplateImpl(jdbcTemplate);
    }

    @Test
    void findById_WhenNewReaderSaved_AssertSavedReaderFromDbNotNull() {
        Reader reader = new Reader(1, "name", LocalDate.parse("1999-01-10"));

        readerDaoJdbcTemplate.save(reader);

        Reader readerDB = readerDaoJdbcTemplate.findById(reader.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("This reader id %d doesn't exist", reader.getId())));

        Assertions.assertNotNull(readerDB);
    }

}
