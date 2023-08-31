package com.example.controllers;

import com.example.entity.Book;
import com.example.service.BookService;
import com.example.service.BorrowService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @MockBean
    private BookService bookService;

    @MockBean
    private BorrowService borrowService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @ParameterizedTest
    @CsvSource(value = {"name, 111, Author must be literal",
                        " , author, Name is required",
                        "name, , Author is required"})
    @DisplayName("Should return bad request when parameter book not valid")
    void saveBook_WhenParamNameOrAuthorInBookNotValid_ThenReturnBadRequest(
            String name, String author, String errorMessage) {
        given()
          .contentType("application/json")
          .body(new Book(name, author, false))
        .when()
          .post("/api/v1/books")
        .then()
          .statusCode(400)
          .body("errors[0].constraint", equalTo(errorMessage));
    }

    @Test
    void saveBook_WhenParamNameAndAuthorInBookIsEmpty_ThenReturnBadRequest() {
        given()
                .contentType("application/json")
                .body(new Book("", "", false))
                .when()
                .post("/api/v1/books")
                .then()
                .statusCode(400)
                .body("errors.constraint", hasItems("Author is required", "Name is required"));
    }

}
