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
public class ExpenseCreateDTO {
    private String title;
    private Double price;
    private Long membershipId;
    private String categoryName;
    private LocalDate expenseDate = LocalDate.now();
    private String methodOfPayment;
    private String currencyCode;
}
