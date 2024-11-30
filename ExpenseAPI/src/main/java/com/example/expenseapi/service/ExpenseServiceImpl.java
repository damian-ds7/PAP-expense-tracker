package com.example.expenseapi.service;

import com.example.expenseapi.pojo.Category;
import com.example.expenseapi.pojo.Expense;
import com.example.expenseapi.pojo.ExpenseNotFound;
import com.example.expenseapi.repository.CategoryRepository;
import com.example.expenseapi.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
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
        Iterable<Expense> expanses = expenseRepository.findAll();
        return StreamSupport.stream(expanses.spliterator(), false)
                .filter(expanse -> expanse.getUser().getEmail().equals(mail))
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
}
