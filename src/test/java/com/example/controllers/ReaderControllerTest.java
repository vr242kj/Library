package com.example.controllers;

import com.example.entity.Reader;
import com.example.service.BorrowService;
import com.example.service.ReaderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@WebMvcTest(ReaderController.class)
class ReaderControllerTest {

    @MockBean
    private ReaderService readerService;

    @MockBean
    private BorrowService borrowService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @ParameterizedTest
    @CsvSource(value = {"name99999@!",
            "name1",
            "name1 name2 name3 name4"})
    @DisplayName("Should return bad request when parameter reader not valid")
    void saveReader_WhenParamNameNotValid_ThenReturnBadRequest(String name) {
        given()
                .contentType("application/json")
                .body(new Reader(name, LocalDate.now()))
                .when()
                .post("/api/v1/readers")
                .then()
                .statusCode(400)
                .body("errors[0].constraint",
                        equalTo("Full name must be literal and one space between words (max 3 words)"));
    }

    @ParameterizedTest
    @CsvSource(value = {"name, , Birthdate is required", " , 1980-01-01, Name is required"})
    @DisplayName("Should return bad request when parameter reader not valid")
    void saveReader_WhenParamNameOrBirthdateIsEmpty_ThenReturnBadRequest(
            String name, LocalDate birthdate, String errorMessage) {
        given()
                .contentType("application/json")
                .body(new Reader(name, birthdate))
                .when()
                .post("/api/v1/readers")
                .then()
                .statusCode(400)
                .body("errors[0].constraint", equalTo(errorMessage));
    }

    @Test
    void saveReader_WhenValidReader_ThenReturnCreated() {
        Reader mockReader = new Reader("John", LocalDate.now());

        when(readerService.addNewReader(mockReader))
                .thenReturn(new Reader(1, "John", LocalDate.now()));

        given()
                .contentType("application/json")
                .body(mockReader)
                .when()
                .post("/api/v1/readers")
                .then()
                .statusCode(201)
                .body("id", equalTo(1))
                .body("name", equalTo("John"))
                .body("birthdate", equalTo(LocalDate.now().toString()));
    }

    @Test
    public void getReaderById_WhenReaderIdExist_ThanReturnReaderWithId() {
        long readerId = 1L;
        Reader mockReader = new Reader(1, "John", LocalDate.now());

        when(readerService.findByReaderId(readerId)).thenReturn(Optional.of(mockReader));

        given()
                .when()
                .get("/api/v1/readers/{id}", readerId)
                .then()
                .statusCode(200)
                .body("id", equalTo((int) mockReader.getId()))
                .body("name", equalTo(mockReader.getName()))
                .body("birthdate", equalTo(mockReader.getBirthdate().toString()));
    }

    @Test
    void getReaderById_WhenReaderIdNotExist_ThenReturnNotFound() {
        given()
                .when()
                .get("/api/v1/readers/{id}", 99999)
                .then()
                .statusCode(404);
    }

    @Test
    void getAllReaders_WhenListOfReadersExist_ThenReturnListOfReaders() throws Exception {
        List<Reader> mockListOfReader = List.of(
                new Reader(1, "John", LocalDate.now()),
                new Reader(2, "Tom", LocalDate.now())
        );

        when(readerService.findAllReaders()).thenReturn(mockListOfReader);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String expectedResponseBody = objectMapper.writeValueAsString(mockListOfReader);

        given()
                .when()
                .get("/api/v1/readers")
                .then()
                .statusCode(200)
                .body(equalTo(expectedResponseBody));
    }

    @Test
    void getAllReaders_WhenListOfReaderNotExist_ThenReturnNoContent() {
        when(readerService.findAllReaders()).thenReturn(List.of());

        given()
                .when()
                .get("/api/v1/readers")
                .then()
                .statusCode(204);
    }

}
