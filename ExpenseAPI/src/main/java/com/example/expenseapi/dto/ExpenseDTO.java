package com.example.expenseapi.dto;

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
    private String categoryName;
    private String methodOfPayment;
    private String currencyCode;
    private String userEmail;
    private String groupName;
}
