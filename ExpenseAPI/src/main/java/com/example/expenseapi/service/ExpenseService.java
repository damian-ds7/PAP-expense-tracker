package com.example.expenseapi.service;

import com.example.expenseapi.pojo.Expense;

import java.util.List;

public interface ExpenseService {
    Expense getExpense(Long id);
    List<Expense> getAllExpenses();
    Expense saveExpense(Expense expense);
    void deleteExpense(Long id);
}
