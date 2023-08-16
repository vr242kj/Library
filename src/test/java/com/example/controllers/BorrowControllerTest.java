package com.example.controllers;

import com.example.entity.Borrow;
import com.example.exception.ResourceNotFoundException;
import com.example.service.BorrowService;
import com.example.service.ServiceException;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@WebMvcTest(BorrowController.class)
class BorrowControllerTest {

    @MockBean
    private BorrowService borrowService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @ParameterizedTest
    @CsvSource(value = {"1, 99999", "99999, 1"})
    void saveBorrow_WhenBookOrReaderIdNotExist_ThenReturnNotFound(long bookId, long readerId) {
        Borrow testBorrow = new Borrow(bookId, readerId);
        when(borrowService.addNewBorrow(testBorrow)).thenThrow(ResourceNotFoundException.class);

        given()
                .contentType("application/json")
                .body(testBorrow)
                .when()
                .post("/api/v1/borrows")
                .then()
                .statusCode(404);
    }

    @Test
    void saveBorrow_WhenCheckBorrowRulesFails_ThenReturnBadRequest() {
        Borrow testBorrow = new Borrow(1, 1);
        when(borrowService.addNewBorrow(testBorrow)).thenThrow(ServiceException.class);

        given()
                .contentType("application/json")
                .body(testBorrow)
                .when()
                .post("/api/v1/borrows")
                .then()
                .statusCode(400);
    }

    @Test
    void updateBorrowReturnBook_WhenBorrowIdNotExist_ThenReturnNotFound() {
        long nonExistentBorrowId = 999L;

        doThrow(new ResourceNotFoundException("This borrow id doesn't exist"))
                .when(borrowService).updateBorrowAndReturnBook(nonExistentBorrowId);

        given()
                .contentType(ContentType.JSON)
                .body(new Borrow())
                .when()
                .put("/api/v1/borrows/{id}", nonExistentBorrowId)
                .then()
                .statusCode(404);
    }

    @Test
    void updateBorrowReturnBook_WhenBorrowIdExist_ThenReturnOk() {
        long borrowId = 1;

        given()
                .contentType(ContentType.JSON)
                .body(new Borrow())
                .when()
                .put("/api/v1/borrows/{id}", borrowId)
                .then()
                .statusCode(200);
    }

    @Test
    void getBorrowById_WhenBorrowIdNotExist_ThenReturnNotFound() {
        given()
                .when()
                .get("/api/v1/borrows/{id}", 99999)
                .then()
                .statusCode(404);
    }

}
