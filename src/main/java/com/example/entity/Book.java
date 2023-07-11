package com.example.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @NotNull
    private long id;

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "[A-Za-z0-9\\s\\-_,\\.:()]*[A-Za-z0-9\\-_,\\.:()]+",
            message = "The field must not be empty, you can use letters, numbers and -_,.:()")
    private String name;

    @NotBlank(message = "Author is required")
    @Pattern(regexp = "[a-zA-Z]+\\s?[a-zA-Z]+\\s?[a-zA-Z]*\\s*",
            message = "Author must be literal")
    private String author;
    private long readerId;

    public Book(long id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public void setId(long id) {
        if (this.id == 0) {
            this.id = id;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setReaderId(long readerId) {
        this.readerId = readerId;
    }

    @Override
    public String toString() {
        return this.readerId == 0 ? "Book{" +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", author = '" + author + "'}" :
                "Book{" +
                        "id = " + id +
                        ", name = '" + name + '\'' +
                        ", author = '" + author + '\'' +
                        ", readerId = " + readerId +
                        "'}";
    }

}
