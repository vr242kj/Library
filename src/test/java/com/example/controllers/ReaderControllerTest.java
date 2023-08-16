package com.example.controllers;

import com.example.entity.Reader;
import com.example.service.BorrowService;
import com.example.service.ReaderService;
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

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
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
            "name1 name2 name3 name4",
            "' '"})
    @DisplayName("Should return bad request when parameter reader not valid")
    void saveReader_WhenParamReaderNotValid_ThenReturnBadRequest(String name) {
        given()
                .contentType("application/json")
                .body(new Reader(name, LocalDate.now()))
                .when()
                .post("/api/v1/readers")
                .then()
                .statusCode(400);
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
    void getAllReaders_WhenListOfReaderNotExist_ThenReturnNoContent() {
        when(readerService.findAllReaders()).thenReturn(List.of());

        given()
                .when()
                .get("/api/v1/readers")
                .then()
                .statusCode(204);
    }

}
