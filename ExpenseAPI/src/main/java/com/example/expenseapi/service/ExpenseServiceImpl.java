package com.example.expenseapi.service;

import com.example.expenseapi.pojo.Expense;
import com.example.expenseapi.pojo.ExpenseNotFound;
import com.example.expenseapi.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ExpenseServiceImpl extends GenericServiceImpl<Expense, Long> implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository repository) {
        super(repository);
        this.expenseRepository = repository;
    }

    public List<Expense> getExpensesByEmail(String mail) {
        Iterable<Expense> expanses = expenseRepository.findAll();
        return StreamSupport.stream(expanses.spliterator(), false)
                .filter(expanse -> expanse.getUser().getEmail().equals(mail))
                .collect(Collectors.toList());
    }
}
