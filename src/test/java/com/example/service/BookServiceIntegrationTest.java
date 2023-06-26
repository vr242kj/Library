package com.example.service;

import com.example.entity.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ConfigurationPropertiesScan("src/test/resources/application-test.yml")
@Sql(value = "classpath:schema.sql", executionPhase = BEFORE_TEST_METHOD)
public class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Book> expectedBooks = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        expectedBooks.add(new Book(1,"In Search of Lost Time", "Marcel Proust"));
        expectedBooks.add(new Book(2, "Ulysses", "James Joyce"));
        expectedBooks.add(new Book(3, "Don Quixote", "Miguel de Cervantes"));

        for (Book book : expectedBooks) {
            jdbcTemplate.update("INSERT INTO book (name, author) VALUES (?, ?)",
                    book.getName(), book.getAuthor());
        }
    }

    @AfterEach
    public void tearDown() {
        jdbcTemplate.execute("DROP TABLE book");
        expectedBooks.clear();
    }

    @Test
    public void testFindAllBooks() {
        List<Book> actualBooks = bookService.findAllBooks();

        assertEquals(3, actualBooks.size());

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }

}
