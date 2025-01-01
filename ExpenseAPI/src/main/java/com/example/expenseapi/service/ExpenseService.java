package com.example.expenseapi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.expenseapi.dto.ExpenseCreateDTO;
import com.example.expenseapi.dto.ExpenseDTO;
import com.example.expenseapi.dto.ExpenseFilter;
import com.example.expenseapi.pojo.Category;
import com.example.expenseapi.pojo.ExpInfo;
import com.example.expenseapi.pojo.Expense;

public interface ExpenseService extends GenericService<Expense, Long> {
    List<ExpenseDTO> getExpensesForGroup(String name);
    List<ExpenseDTO> getExpensesForUser();
    ExpInfo getExpInfo(String group);
    ExpInfo getExpInfo();
    Map<String, Double> getMonthlyExpensesForUser(String year, String currCode);
    Map<String, Double> getMonthlyExpensesForGroup(String year, String currCode);
    Map<String, Double> getSumOfCategoryExpansesForGroup(String begin, String end, String currCode);
    Map<String, Double> getSumOfCategoryExpansesForUser(String begin, String end, String currCode);
    Optional<ExpenseDTO> getRecentExpense(String groupName);
    Map<LocalDate, List<ExpenseDTO>> getGroupExpenseAsDateMap(String name);
    Map<Category, List<ExpenseDTO>> getGroupExpenseAsCategoryMap(String name);
    List<ExpenseDTO> searchExpensesDTO(ExpenseFilter filter);
    ExpenseDTO createExpense(ExpenseCreateDTO createDTO);
}