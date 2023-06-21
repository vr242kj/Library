package com.example.service;

import com.example.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ConfigurationPropertiesScan("src/test/resources/application-test.yml")
@Sql(value = "classpath:schema-test.sql", executionPhase = BEFORE_TEST_METHOD)
public class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Test
    public void testFindAllBooks() {
        List<Book> expectedBooks = List.of(
                new Book(1,"In Search of Lost Time", "Marcel Proust"),
                new Book(2, "Ulysses", "James Joyce"),
                new Book(3, "Don Quixote", "Miguel de Cervantes")
        );

        List<Book> actualBooks = bookService.findAllBooks();

        assertEquals(3, actualBooks.size());

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }

}
