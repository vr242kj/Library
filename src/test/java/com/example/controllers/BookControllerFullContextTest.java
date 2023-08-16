package com.example.controllers;

import com.example.entity.Book;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase
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
    @DisplayName("Should successfully save new book")
    void saveBook() throws Exception {
        Book expectedBook = new Book(6, "Don Quixote", "Miguel de Cervantes",14,  false);

        ExtractableResponse<Response> response =
                    given()
                        .contentType("application/json")
                        .body(new Book("Don Quixote", "Miguel de Cervantes", false))
                    .when()
                        .post()
                    .then()
                        .statusCode(201)
                        .extract();

        int userId =
                when()
                    .get(response.header("Location"))
                .then()
                    .statusCode(200)
                    .body("id", notNullValue())
                    .body("name", equalTo(expectedBook.getName()))
                    .body("author", equalTo(expectedBook.getAuthor()))
                    .body("restricted", equalTo(expectedBook.isRestricted()))
                    .extract()
                    .path("id");

        Book bookInDB = retrieveCreatedBookInDb(userId);

        assertEquals(expectedBook, bookInDB);
    }

    private Book retrieveCreatedBookInDb(int userId) throws Exception {
        return jdbcTemplate.query("select * from book where id = ?",
                        (resultSet, rowNum) -> new Book(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getString("author"),
                                resultSet.getInt("maxborrowtimeindays"),
                                resultSet.getBoolean("restricted")
                        ), userId)
                .stream().findFirst().orElseThrow(() -> new Exception("This book id doesn't exist"));
    }

}
