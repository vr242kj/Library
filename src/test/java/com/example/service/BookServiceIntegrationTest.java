package com.example.service;

import com.example.dao.BookDaoJdbcTemplateImpl;
import com.example.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ConfigurationPropertiesScan("src/test/resources/application-test.yml")
@Sql(value = "classpath:schema-test.sql", executionPhase = BEFORE_TEST_METHOD)
public class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookDaoJdbcTemplateImpl bookDaoJdbcTemplate;

    @Test
    public void testFindAllBooks() {
        List<Book> repoBooks = bookDaoJdbcTemplate.findAll();
        List<Book> serviceBooks = bookService.findAllBooks();

        assertEquals(serviceBooks, repoBooks);
    }
}
