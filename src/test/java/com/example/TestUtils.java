package com.example;

import com.example.entity.Book;
import com.example.entity.Borrow;

import java.time.LocalDate;
import java.util.List;

public class TestUtils {

    public List<Borrow> generateListOfBorrow() {
        return  List.of(
                new Borrow(1, 1, 1, LocalDate.parse("2020-01-08"),
                        LocalDate.parse("2020-01-10"), LocalDate.parse("2020-01-18")),
                new Borrow(2, 2, 1, LocalDate.parse("2020-01-08"),
                        LocalDate.parse("2020-01-10"), LocalDate.parse("2020-01-18"))
        );
    }

    public List<Borrow> generateListOfBorrowWhereBookBorrowed() {
        return  List.of(
                new Borrow(1, 1, 1,
                        LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-18")),
                new Borrow(2, 2, 1,
                        LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-18"))
        );
    }

    public Borrow generateBorrow() {
        return  new Borrow(1, 1, 1, LocalDate.parse("2020-01-08"),
                LocalDate.parse("2020-01-10"), LocalDate.parse("2020-01-18"));
    }

    public static List<Book> generateListOfBook() {
        return  List.of(
                new Book(1, "XXX", "XXX", 10, false),
                new Book(2, "YYY", "YYY", 10, false)
        );
    }

}
