package com.example.controllers;

import com.example.entity.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@AutoConfigureTestDatabase
@Sql(value = "classpath:schema.sql", executionPhase = BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerFullContextTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/book-library/api/v1/books";
    }

    @Test
    void saveBook() throws Exception {
        Book expectedBook = new Book(1, "Don Quixote", "Miguel de Cervantes");

        int userId =
                given().
                        contentType("application/json").
                        body(expectedBook).
                        when().
                        post().
                        then().
                        statusCode(201).
                        extract().
                        path("id");

        assertEquals(expectedBook.getId(), userId);

        Book bookInDB = jdbcTemplate.query("select * from book where id = ?",
                        (resultSet, rowNum) -> new Book(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getString("author")
                        ), userId)
                .stream().findFirst().orElseThrow(() -> new Exception("This book id doesn't exist"));

        assertEquals(expectedBook, bookInDB);
    }

    @Test
    void saveBook_withRestAssure() throws JsonProcessingException {
        Book book = new Book("Don Quixote", "Miguel de Cervantes");

        given()
                .contentType(ContentType.JSON)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo(book.getName()))
                .body("author", equalTo(book.getAuthor()));
    }

}
