package com.example.dao;

import com.example.entity.Reader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Reader mockReader = new Reader( "name", LocalDate.parse("1999-01-10"));

        var generatedId = readerDaoJdbcTemplate.save(mockReader).getId();

        Optional<Reader> readerDB = readerDaoJdbcTemplate.findById(generatedId);

        // Assert
        assertAll("Saved reader information",
                () -> assertTrue(readerDB.isPresent(), "Reader should be present"),
                () -> assertEquals(generatedId, readerDB.map(Reader::getId)
                        .orElse(null), "IDs should match"),
                () -> assertEquals(mockReader.getName(), readerDB.map(Reader::getName)
                        .orElse(null), "Names should match"),
                () -> assertEquals(mockReader.getBirthdate(), readerDB.map(Reader::getBirthdate)
                        .orElse(null), "Birthdates should match")
        );
    }

}
