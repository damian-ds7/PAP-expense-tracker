package com.example.expenseapi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.expenseapi.dto.ExpenseFilter;
import com.example.expenseapi.pojo.Category;
import com.example.expenseapi.pojo.ExpInfo;
import com.example.expenseapi.pojo.Expense;

public interface ExpenseService extends GenericService<Expense, Long> {
    List<Expense> getExpensesByEmail(String email);
    List<Expense> getExpensesForGroup(String name);
    ExpInfo getExpInfo(String group);
    ExpInfo getExpInfo();
    Map<String, Double> getMonthlyExpensesForUser(String year, String currCode);
    Map<String, Double> getMonthlyExpensesForGroup(String year, String currCode);
    Map<String, Double> getSumOfCategoryExpansesForGroup(String begin, String end, String currCode);
    Map<String, Double> getSumOfCategoryExpansesForUser(String begin, String end, String currCode);
    Optional<Expense> getRecentExpense();
    Map<LocalDate, List<Expense>> getGroupExpenseAsDateMap(String name);
    Map<Category, List<Expense>> getGroupExpenseAsCategoryMap(String name);
    List<Expense> searchExpenses(ExpenseFilter filter);
    List<Expense> getExpensesForGroup();
}