package com.example.controllers;

import com.example.entity.Book;
import com.example.service.BookService;
import com.example.service.ReaderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @MockBean
    private BookService bookService;

    @MockBean
    private ReaderService readerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @ParameterizedTest
    @CsvSource(value = {"' ', ' '", "name, 111"})
    @DisplayName("Should return bad request when parameter book not valid")
    void saveBook_WhenParamBookNotValid_ThenReturnBadRequest(String name, String author) {
        given()
          .contentType("application/json")
          .body(new Book(name, author))
        .when()
          .post("/api/v1/books")
        .then()
          .statusCode(400);
    }

}
