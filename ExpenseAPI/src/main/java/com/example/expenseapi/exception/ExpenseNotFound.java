package com.example.expenseapi.exception;

public class ExpenseNotFound extends RuntimeException{
    public ExpenseNotFound(Long id) {
        super("Expense with id " + id + " not found");
    }
}
