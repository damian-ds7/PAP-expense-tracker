package com.example.expenseapi.service;

import java.util.List;

import com.example.expenseapi.pojo.ExpInfo;
import com.example.expenseapi.pojo.Expense;

public interface ExpenseService extends GenericService<Expense, Long> {
    List<Expense> getExpensesByEmail(String email);
    List<Expense> getExpensesByCategory(String category);
    List<Expense> getExpensesByDate(String date);
    List<Expense> getExpensesByPeriod(String begin, String end);
    List<Expense> getExpensesWherePriceInRange(double left_end, double right_end);
    List<Expense> getExpensesWherePriceIsLower(double price);
    List<Expense> getExpensesWherePriceIsGreater(double price);

    List<Expense> getExpensesForGroup(String name);

    ExpInfo getExpInfo(String name, String userId);
}