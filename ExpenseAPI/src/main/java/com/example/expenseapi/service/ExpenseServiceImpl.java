package com.example.expenseapi.service;

import com.example.expenseapi.pojo.Expense;
import com.example.expenseapi.repository.ExpenseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ExpenseServiceImpl implements ExpenseService{

    ExpenseRepository expenseRepository;

    @Override
    public Expense getExpense(Long id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        return unwrapExpense(expense, id);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return (List<Expense>) expenseRepository.findAll();
    }

    @Override
    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
    static Expense unwrapExpense(Optional<Expense> expense, Long id) {
        return expense.orElse(null);
    }
}
