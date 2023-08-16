package com.example.dao;

import com.example.entity.Borrow;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
class BorrowDaoJdbcTemplateImplTest {

    private static BorrowDaoJdbcTemplateImpl borrowDaoJdbcImpl;

    @BeforeAll
    public static void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        borrowDaoJdbcImpl = new BorrowDaoJdbcTemplateImpl(jdbcTemplate);
    }

    @Test
    void testFindAllBorrows_WhenBorrowExists_ReturnSavedBorrows() {
        List<Borrow> expectedBorrows = List.of(
                new Borrow(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(5)),
                new Borrow(2, 2, 2, LocalDate.now(), LocalDate.now().plusDays(10))
        );

        borrowDaoJdbcImpl.save(expectedBorrows.get(0));
        borrowDaoJdbcImpl.save(expectedBorrows.get(1));

        List<Borrow> actualBorrows = borrowDaoJdbcImpl.findAll();

        assertEquals(expectedBorrows, actualBorrows);
    }

}
