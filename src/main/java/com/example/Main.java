package com.example;

import com.example.dao.BookDao;
import com.example.entity.Book;
import com.example.entity.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Main {

    @Autowired
    private BookDao bookDao;
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public void runApplication() {

        List<Book> allBooks = bookDao.findAll();
        System.out.println("All Books: " + allBooks);

        Optional<Book> bookById = bookDao.findById(164);
        System.out.println("Book with ID 1: " + bookById.orElse(null));

        List<Book> booksByReaderId = bookDao.findAllByReaderId(2L);
        System.out.println("Books with Reader ID 2: " + booksByReaderId);

        Book bookToSave = new Book("Book Name", "Book Author");
        Book savedBook = bookDao.save(bookToSave);
        System.out.println("Saved Book: " + savedBook);

        bookDao.borrowBookToReader(savedBook.getId(), 38L);

        bookDao.returnBookToLibrary(163);

        Map<Book, Optional<Reader>> booksWithReaders = bookDao.findAllWithReaders();
        System.out.println("Books with Readers: " + booksWithReaders);

        bookDao.deleteById(savedBook.getId());
    }
}
