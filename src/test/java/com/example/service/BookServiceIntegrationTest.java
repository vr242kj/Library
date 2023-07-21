package com.example.service;

import com.example.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
public class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    private List<Book> expectedBooks = new ArrayList<>();

    @BeforeEach
    public void setUp() {
//        expectedBooks.add(new Book(1,"In Search of Lost Time", "Marcel Proust", 1));
//        expectedBooks.add(new Book(2, "Ulysses", "James Joyce", 3));
//        expectedBooks.add(new Book(3, "Don Quixote", "Miguel de Cervantes", 3));
//        expectedBooks.add(new Book(4, "Moby Dick", "Herman Melville"));
//        expectedBooks.add(new Book(5, "Hamlet", "William Shakespeare"));
    }

    @Test
    @DisplayName("Should successfully retrieve all books")
    public void findAllBooks() {
        List<Book> actualBooks = bookService.findAllBooks();

        assertEquals(5, actualBooks.size());

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }

}
