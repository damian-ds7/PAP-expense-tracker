package com.example.expenseapi.dto;

import com.example.expenseapi.pojo.Category;
import com.example.expenseapi.pojo.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ExpenseDTO {
    private Long id;
    private String title;
    private Double price;
    private LocalDate expenseDate;
    private Category category;
    private String methodOfPayment;
    private Currency currency;
    private String userEmail;
    private String name;
    private String surname;
    private String groupName;
}
