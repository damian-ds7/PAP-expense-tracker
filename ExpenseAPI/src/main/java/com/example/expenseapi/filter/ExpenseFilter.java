package com.example.expenseapi.filter;

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
    private List<String> categoryNames;
    private LocalDate date;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Double priceMin;
    private Double priceMax;
    private List<String> methodsOfPayment;
    private String groupName;
    private String email;
    private List<String> emails;
}
