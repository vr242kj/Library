package com.example.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Reader {

    private long id;
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "[a-zA-Z]+\\s?[a-zA-Z]+\\s?[a-zA-Z]*",
            message = "Full name must be literal and one space between words (max 3 words)")
    private String name;
    private LocalDate birthdate;

    public Reader(String name) {
        this.name = name;
    }

    public void setId(long id) {
        if (this.id == 0) {
            this.id = id;
        }
    }

}
