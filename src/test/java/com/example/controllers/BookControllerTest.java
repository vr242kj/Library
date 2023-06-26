package com.example.controllers;

import com.example.entity.Book;
import com.example.service.BookService;
import com.example.service.ReaderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @ParameterizedTest
    @CsvSource(value = {"' ', ' '", "name, 111"})
    void saveBook_WhenParamBookNotValid_ThenReturnBadRequest(String name, String author) throws Exception {
        Book invalidBook = new Book(name, author);

        mockMvc.perform(post("/api/v1/books")
                        .content(objectMapper.writeValueAsString(invalidBook)))
                        .andExpect(status().isBadRequest());
    }

}
