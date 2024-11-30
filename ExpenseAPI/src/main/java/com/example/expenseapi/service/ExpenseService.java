package com.example.expenseapi.service;

import java.util.List;
import com.example.expenseapi.pojo.Expense;

public interface ExpenseService extends GenericService<Expense, Long> {
    public List<Expense> getExpensesByEmail(String email);
}