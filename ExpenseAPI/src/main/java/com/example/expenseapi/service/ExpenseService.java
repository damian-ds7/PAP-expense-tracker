package com.example.expenseapi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.expenseapi.dto.CursorPageResponse;
import com.example.expenseapi.dto.ExpenseCreateDTO;
import com.example.expenseapi.dto.ExpenseDTO;
import com.example.expenseapi.filter.ExpenseFilter;
import com.example.expenseapi.pojo.Category;
import com.example.expenseapi.pojo.ExpInfo;
import com.example.expenseapi.pojo.Expense;
import org.springframework.data.domain.Page;

public interface ExpenseService extends GenericService<Expense, Long> {
    Page<ExpenseDTO> getExpensesForGroup(String name, int page, int size);
    ExpInfo getExpInfo(String group);
    ExpInfo getExpInfo();
    Map<String, Double> getMapResult(ExpenseFilter filter, String keyType);
    Optional<ExpenseDTO> getRecentExpense(String groupName);
    CursorPageResponse<Map<LocalDate, List<ExpenseDTO>>> getGroupExpenseAsDateMap(String name, Long lastId, LocalDate lastDate, int size, boolean desc);
    Map<Category, List<ExpenseDTO>> getGroupExpenseAsCategoryMap(String name, int page, int size);
    List<ExpenseDTO> searchExpensesDTO(ExpenseFilter filter);
    ExpenseDTO createExpense(ExpenseCreateDTO createDTO);
    ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO);
    List<String> getPatternKeys();
    Page<ExpenseDTO> getExpensesForUser(int page, int size);
    Page<ExpenseDTO> searchExpensesPagesDTO(ExpenseFilter filter, int page, int size);
}