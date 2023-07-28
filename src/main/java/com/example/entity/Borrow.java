package com.example.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Borrow {

    private long id;
    @NotNull
    private long bookId;
    @NotNull
    private long readerId;
    private LocalDate borrowStartDate;
    private LocalDate borrowEndDate;
    private LocalDate expectedReturn;

    public void setId(long id) {
        if (this.id == 0) {
            this.id = id;
        }
    }

}
