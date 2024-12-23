package com.example.expenseapi.service;

import com.example.expenseapi.pojo.*;
import com.example.expenseapi.pojo.Currency;
import com.example.expenseapi.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.*;

@Service
public class ExpenseServiceImpl extends GenericServiceImpl<Expense, Long> implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final MethodOfPaymentRepository methodOfPaymentRepository;

    public ExpenseServiceImpl(ExpenseRepository repository, CategoryRepository categoryRepository, CurrencyRepository currencyRepository, UserRepository userRepository, MembershipRepository membershipRepository, MethodOfPaymentRepository methodOfPaymentRepository) {
        super(repository);
        this.expenseRepository = repository;
        this.categoryRepository = categoryRepository;
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
        this.methodOfPaymentRepository = methodOfPaymentRepository;
    }

    @Override
    public List<Expense> getExpensesByEmail(String mail) {
        return expenseRepository.findByUserEmail(mail);
    }

    @Override
    public Expense save(Expense entity) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<User> user = userRepository.findByEmail(email);
        user.ifPresent(entity::setUser);
        if (entity.getCategory() == null) {
            Category defaultCategory = categoryRepository.findById(1L)
                    .orElseGet(() -> categoryRepository.save(new Category()));
            entity.setCategory(defaultCategory);
        }
        if (entity.getMethod() == null) {
            MethodOfPayment defaultMethod = methodOfPaymentRepository.findById(1L)
                    .orElseGet(() -> methodOfPaymentRepository.save(new MethodOfPayment()));
            entity.setMethod(defaultMethod);
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
        return expenseRepository.findByCategoryName(category, getGroupName());
    }

    @Override
    public List<Expense> getExpensesByDate(String date) {
        LocalDate dateObject = LocalDate.parse(date);
        return expenseRepository.findByDate(dateObject, getGroupName());
    }

    @Override
    public List<Expense> getExpensesByPeriod(String begin, String end) {
        LocalDate beginDate = LocalDate.parse(begin);
        LocalDate endDate = LocalDate.parse(end);
        return expenseRepository.findByDateBetween(beginDate, endDate, getGroupName());
    }

    @Override
    public List<Expense> getExpensesWherePriceInRange(double left_end, double right_end) {
        return expenseRepository.findByPriceBetween(left_end, right_end, getGroupName());
    }

    @Override
    public List<Expense> getExpensesWherePriceIsLower(double price) {
        return expenseRepository.findByPriceLessThan(price, getGroupName());
    }

    @Override
    public List<Expense> getExpensesWherePriceIsGreater(double price) {
        return expenseRepository.findByPriceGreaterThan(price, getGroupName());
    }

    @Override
    public List<Expense> getExpensesForGroup(String name) {
        return expenseRepository.findByUserGroupName(name);
    }

    @Override
    public ExpInfo getExpInfo() {
        List<Expense> groupExpenses = getExpensesForGroup();
        List<Expense> userExpenses = expenseRepository.findByUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        double groupSum = groupExpenses.stream().mapToDouble(Expense::getPrice).sum();
        double userSum = userExpenses.stream().mapToDouble(Expense::getPrice).sum();
        return new ExpInfo(userSum, groupSum);
    }

    @Override
    public Map<LocalDate, List<Expense>> getDateExpenseAsMap() {
        List<Expense> groupExpenses = getExpensesForGroup();
        return groupExpenses.stream()
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .collect(Collectors.groupingBy(
                        Expense::getDate,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    @Override
    public Map<Category, List<Expense>> getCategoryExpenseAsMap() {
        List<Expense> groupExpenses = getExpensesForGroup();
        return groupExpenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory
                ));
    }

    @Override
    public Map<String, Double> getMonthlyExpensesForUser(String year) {
        List<Object[]> results = expenseRepository.findTotalExpensesForMonthsUser(year, SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        Map<String, Double> monthlyExpenses = new LinkedHashMap<>();
        for (Object[] result : results) {
            monthlyExpenses.put((String) result[0], (Double) result[1]);
        }
        return monthlyExpenses;
    }

    @Override
    public Map<String, Double> getMonthlyExpensesForGroup(String year) {
        List<Object[]> results = expenseRepository.findTotalExpensesForMonthsGroup(year, getGroupName());
        Map<String, Double> monthlyExpenses = new LinkedHashMap<>();
        for (Object[] result : results) {
            monthlyExpenses.put((String) result[0], (Double) result[1]);
        }
        return monthlyExpenses;
    }

    @Override
    public Optional<Expense> getRecentExpense() {
        return expenseRepository.findTopByOrderByIdDesc();
    }

    private List<Expense> getExpensesForGroup() {
        return getExpensesForGroup(getGroupName());
    }

    private String getGroupName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String name = null;
        if (user.isPresent()) {
            name = membershipRepository.findBaseGroupsByUser_Id(user.get().getId()).getFirst().getName();
        }
        return name;
    }
}
