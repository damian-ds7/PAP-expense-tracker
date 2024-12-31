package com.example.expenseapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseFilter {
    private String categoryName;
    private LocalDate date;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Double priceMin;
    private Double priceMax;
    private String groupName;
    private String email;
    private List<String> emails;
}
