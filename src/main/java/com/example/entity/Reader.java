package com.example.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

public class Reader  {
    @NotNull
    private long id;
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "[a-zA-Z]+\\s?[a-zA-Z]+\\s?[a-zA-Z]*",
            message = "Full name must be literal and one space between words (max 3 words)")
    private String name;

    public Reader(){}
    public Reader(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Reader(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return id == reader.id && Objects.equals(name, reader.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return this.id == 0 ? "Reader{name = " + name + '}' :
                "Reader{" +
                        "id = " + id +
                        ", name = " + name +
                        "'}";

    }
}
