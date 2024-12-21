package com.example.expenseapi.service;

import com.example.expenseapi.pojo.*;
import com.example.expenseapi.pojo.Currency;
import com.example.expenseapi.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.*;

@Service
public class ExpenseServiceImpl extends GenericServiceImpl<Expense, Long> implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final CurrencyRepository currencyRepository;

    public ExpenseServiceImpl(ExpenseRepository repository, CategoryRepository categoryRepository, GroupRepository groupRepository, MembershipRepository membershipRepository, CurrencyRepository currencyRepository) {
        super(repository);
        this.expenseRepository = repository;
        this.categoryRepository = categoryRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public List<Expense> getExpensesByEmail(String mail) {
        return expenseRepository.findByUserEmail(mail);
    }

    @Override
    public Expense save(Expense entity) {
        if (entity.getCategory() == null) {
            Category defaultCategory = categoryRepository.findById(1L)
                    .orElseGet(() -> categoryRepository.save(new Category()));
            entity.setCategory(defaultCategory);
        }
        if (entity.getCurrency() == null) {
            Currency defaultCurrency = currencyRepository.findById(1L)
                    .orElseGet(() -> currencyRepository.save(new Currency()));
            entity.setCurrency(defaultCurrency);
        }
        return super.save(entity);
    }

    @Override
    public List<Expense> getExpensesByCategory(String category) {
        return expenseRepository.findByCategoryName(category);
    }

    @Override
    public List<Expense> getExpensesByDate(String date) {
        LocalDate dateObject = LocalDate.parse(date);
        return expenseRepository.findByDate(dateObject);
    }

    @Override
    public List<Expense> getExpensesByPeriod(String begin, String end) {
        LocalDate beginDate = LocalDate.parse(begin);
        LocalDate endDate = LocalDate.parse(end);
        return expenseRepository.findByDateBetween(beginDate, endDate);
    }

    @Override
    public List<Expense> getExpensesWherePriceInRange(double left_end, double right_end) {
        return expenseRepository.findByPriceBetween(left_end, right_end);
    }

    @Override
    public List<Expense> getExpensesWherePriceIsLower(double price) {
        return expenseRepository.findByPriceLessThan(price);
    }

    @Override
    public List<Expense> getExpensesWherePriceIsGreater(double price) {
        return expenseRepository.findByPriceGreaterThan(price);
    }

    @Override
    public List<Expense> getExpensesForGroup(String name) {
        return expenseRepository.findByUserGroupName(name);
    }

    @Override
    public ExpInfo getExpInfo(String name, String email) {
        List<Expense> groupExpenses = getExpensesForGroup(name);
        List<Expense> userExpenses = expenseRepository.findByUserEmail(email);
        double groupSum = groupExpenses.stream().mapToDouble(Expense::getPrice).sum();
        double userSum = userExpenses.stream().mapToDouble(Expense::getPrice).sum();
        return new ExpInfo(userSum, groupSum);
    }

    @Override
    public Map<LocalDate, List<Expense>> getDateExpenseAsMap() {
        return expenseRepository.findAllByOrderByDateDesc().stream()
                .collect(Collectors.groupingBy(
                        Expense::getDate,
                        () -> new TreeMap<>(Comparator.reverseOrder()),
                        Collectors.toList()
                ));
    }

    @Override
    public Map<Category, List<Expense>> getCategoryExpenseAsMap() {
        return expenseRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory
                ));
    }

    @Override
    public Optional<Expense> getRecentExpense() {
        return expenseRepository.findTopByOrderByIdDesc();
    }
}
