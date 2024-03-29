package com.example.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private long id;
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "[A-Za-z0-9\\s\\-_,\\.:()]*[A-Za-z0-9\\-_,\\.:()]*",
            message = "Use letters, numbers and -_,.:()!")
    private String name;

    @NotBlank(message = "Author is required")
    @Pattern(regexp = "[a-zA-Z]*\\s?[a-zA-Z]*\\s?[a-zA-Z]*\\s*",
            message = "Author must be literal")
    private String author;
    int maxBorrowTimeInDay;
    @NotNull(message = "Restricted value is required")
    boolean restricted;
    public Book(String name, String author, boolean restricted) {
        this.name = name;
        this.author = author;
        this.restricted = restricted;
    }

    public void setId(long id) {
        if (this.id == 0) {
            this.id = id;
        }
    }

    public void setMaxBorrowTimeInDay(int maxBorrowTimeInDay) {
        this.maxBorrowTimeInDay = maxBorrowTimeInDay;
    }

}
