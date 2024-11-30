package com.example.expenseapi.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpInfo {
    private double userExpenses;
    private double expenses;
    public ExpInfo(double userExpenses, double expenses) {
        this.userExpenses = userExpenses;
        this.expenses = expenses;
    }

}
