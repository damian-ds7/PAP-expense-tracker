package com.example.expenseapi.pojo;

public class ExpenseNotFound extends RuntimeException{
    public ExpenseNotFound(Long id) {
        super("Expense with id " + id + " not found");
    }
}
