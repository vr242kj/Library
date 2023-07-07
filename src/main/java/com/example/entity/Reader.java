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
public class Reader {
    @NotNull
    private long id;
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "[a-zA-Z]+\\s?[a-zA-Z]+\\s?[a-zA-Z]*",
            message = "Full name must be literal and one space between words (max 3 words)")
    private String name;

    public Reader(String name) {
        this.name = name;
    }

    public void setId(long id) {
        if (this.id == 0) {
            this.id = id;
        }
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
