package com.example.expenseapi.service;

import com.example.expenseapi.pojo.*;
import com.example.expenseapi.repository.CategoryRepository;
import com.example.expenseapi.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ExpenseServiceImpl extends GenericServiceImpl<Expense, Long> implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseServiceImpl(ExpenseRepository repository, CategoryRepository categoryRepository) {
        super(repository);
        this.expenseRepository = repository;
        this.categoryRepository = categoryRepository;
    }

    public List<Expense> getExpensesByEmail(String mail) {
        Iterable<Expense> expenses = expenseRepository.findAll();
        return StreamSupport.stream(expenses.spliterator(), false)
                .filter(expense -> expense.getUser().getEmail().equals(mail))
                .collect(Collectors.toList());
    }

    @Override
    public Expense save(Expense entity) {
        if (entity.getCategory() == null) {
            if (categoryRepository.findById(1L).isPresent())
                entity.setCategory(categoryRepository.findById(1L).get());
            else {
                categoryRepository.save(new Category());
                entity.setCategory(categoryRepository.findById(1L).get());
            }
        }
        return super.save(entity);
    }

    @Override
    public List<Expense> getExpensesByCategory(String category) {
        Iterable<Expense> expenses = expenseRepository.findAll();
        return StreamSupport.stream(expenses.spliterator(), false)
                .filter(expense -> expense.getCategory().getName().equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Expense> getExpensesByDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        Iterable<Expense> expenses = expenseRepository.findAll();
        return StreamSupport.stream(expenses.spliterator(), false)
                .filter(expense -> expense.getDate().equals(localDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Expense> getExpensesByPeriod(String begin, String end) {
        LocalDate beginDate = LocalDate.parse(begin);
        LocalDate endDate = LocalDate.parse(end);
        Iterable<Expense> expenses = expenseRepository.findAll();
        return StreamSupport.stream(expenses.spliterator(), false)
                .filter(expense -> !expense.getDate().isBefore(beginDate) && !expense.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
}
