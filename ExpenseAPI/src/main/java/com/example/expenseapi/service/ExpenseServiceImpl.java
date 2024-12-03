package com.example.expenseapi.service;

import com.example.expenseapi.pojo.*;
import com.example.expenseapi.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.*;

@Service
public class ExpenseServiceImpl extends GenericServiceImpl<Expense, Long> implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserGroupRepository userGroupRepository;

    public ExpenseServiceImpl(ExpenseRepository repository, CategoryRepository categoryRepository, UserGroupRepository userGroupRepository) {
        super(repository);
        this.expenseRepository = repository;
        this.categoryRepository = categoryRepository;
        this.userGroupRepository = userGroupRepository;
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
            Category defaultCategory = categoryRepository.findById(1L)
                    .orElseGet(() -> categoryRepository.save(new Category()));
            entity.setCategory(defaultCategory);
        }
        if (entity.getUser() != null && entity.getUser().getUserGroup() == null) {
            UserGroup defaultUserGroup = userGroupRepository.findById(1L)
                    .orElseGet(() -> userGroupRepository.save(new UserGroup()));
            entity.getUser().setUserGroup(defaultUserGroup);
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

    @Override
    public List<Expense> getExpensesWherePriceInRange(double left_end, double right_end) {
        Iterable<Expense> expenses = expenseRepository.findAll();
        return StreamSupport.stream(expenses.spliterator(), false)
                .filter(expense -> expense.getPrice() >= left_end && expense.getPrice() <= right_end)
                .collect(Collectors.toList());
    }

    @Override
    public List<Expense> getExpensesWherePriceIsLower(double price) {
        return getExpensesWherePriceInRange(0, price);
    }

    @Override
    public List<Expense> getExpensesWherePriceIsGreater(double price) {
        return getExpensesWherePriceInRange(price, Double.POSITIVE_INFINITY);
    }

    @Override
    public List<Expense> getExpensesForGroup(String name) {
        Iterable<Expense> expenses = expenseRepository.findAll();
        return StreamSupport.stream(expenses.spliterator(), false)
                .filter(expense -> expense.getUser().getUserGroup().getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public ExpInfo getExpInfo(String name, String email) {
        List<Expense> groupExpenses = getExpensesForGroup(name);
        List<Expense> userExpenses = groupExpenses.stream()
                .filter(expense -> expense.getUser().getEmail().equals(email))
                .toList();
        double groupSum = groupExpenses.stream().mapToDouble(Expense::getPrice).sum();
        double userSum = userExpenses.stream().mapToDouble(Expense::getPrice).sum();
        return new ExpInfo(userSum, groupSum);
    }

    @Override
    public Map<LocalDate, List<Expense>> getDateExpenseAsMap() {
        return StreamSupport.stream(expenseRepository.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(
                        Expense::getDate,
                        () -> new TreeMap<>(Comparator.reverseOrder()),
                        Collectors.toList()
                ));
    }

    @Override
    public Map<Category, List<Expense>> getCategoryExpenseAsMap() {
        return StreamSupport.stream(expenseRepository.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(
                        Expense::getCategory
                ));
    }

    @Override
    public Optional<Expense> getRecentExpense() {
        return StreamSupport.stream(expenseRepository.findAll().spliterator(), false)
                .max(Comparator.comparingLong(Expense::getId));
    }
}
