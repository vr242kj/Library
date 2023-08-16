package com.example.service;

import com.example.dao.BookDaoJdbcTemplateImpl;
import com.example.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceUnitTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookDaoJdbcTemplateImpl bookDaoJdbcTemplate;

    @Test
    @DisplayName("Should successfully retrieve all books")
    void findAllBooks() {
        List<Book> actualBooks = List.of(
                new Book(1, "XXX", "XXX", 10, false),
                new Book(2, "YYY", "YYY", 10, false)
        );

        when(bookDaoJdbcTemplate.findAll()).thenReturn(actualBooks);

        List<Book> expectedBooks = bookService.findAllBooks();

        assertEquals(expectedBooks, actualBooks);
    }

}
