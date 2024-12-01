package com.example.expenseapi.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpInfo {
    private double userExpenses;
    private double groupExpenses;
    public ExpInfo(double userExpenses, double expenses) {
        this.userExpenses = userExpenses;
        this.groupExpenses = expenses;
    }

}
