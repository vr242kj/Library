package com.example.controllers;

import com.example.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ConfigurationPropertiesScan("src/test/resources/application-test.yml")
@Sql(value = "classpath:schema.sql", executionPhase = BEFORE_TEST_METHOD)
class BookControllerFullContextTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final Book expectedBook = new Book(1, "Don Quixote", "Miguel de Cervantes");

    @Test
    void saveBook_TestControllerReturn() throws Exception {
        ResponseEntity<Book> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/book-library/api/v1/books", expectedBook, Book.class);

        assertEquals(expectedBook.getId(), Objects.requireNonNull(responseEntity.getBody()).getId());

        Book bookInDB = jdbcTemplate.query("select * from book where id = ?",
                        (resultSet, rowNum) -> new Book(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getString("author")
                        ), Objects.requireNonNull(responseEntity.getBody()).getId())
                .stream().findFirst().orElseThrow(() -> new Exception("This book id doesn't exist"));

        assertEquals(expectedBook, bookInDB);
    }

}
