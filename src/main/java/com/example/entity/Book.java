package com.example.entity;

import java.util.Objects;

import java.util.Objects;

public class Book {
    private long id;
    private String name;
    private String author;
    private long readerId;

    public Book(long id, String name, String author, long readerId) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.readerId = readerId;
    }

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

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && readerId == book.readerId && Objects.equals(name, book.name) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, readerId);
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