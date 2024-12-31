package com.example.expenseapi.service;

import com.example.expenseapi.ExpenseApiApplication;
import com.example.expenseapi.dto.ExpenseFilter;
import com.example.expenseapi.pojo.*;
import com.example.expenseapi.pojo.Currency;
import com.example.expenseapi.repository.*;
import com.example.expenseapi.utils.ExpenseSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
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
    public Expense save(Expense entity) {
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // String email = auth.getName();
        // Optional<User> user = userRepository.findByEmail(email);
        // user.ifPresent(entity::setUser);
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
    public List<Expense> getExpensesForGroup(String name) {
        ExpenseFilter filter = new ExpenseFilter();
        filter.setGroupName(name);
        return searchExpenses(filter);
    }

    @Override
    public ExpInfo getExpInfo(String group) {
        List<Expense> groupExpenses = getExpensesForGroup(group);
        List<Expense> userExpenses = groupExpenses.stream()
                .filter(expense -> expense.getMembership().getUser().getEmail().equals(getUserEmail()))
                .toList(); // Returns only user's expenses in provided group, not in all groups
        double groupSum = groupExpenses.stream().mapToDouble(Expense::getPrice).sum();
        double userSum = userExpenses.stream().mapToDouble(Expense::getPrice).sum();
        return new ExpInfo(userSum, groupSum);
    }

    @Override
    public ExpInfo getExpInfo() {
        List<BaseGroup> groups = getAllGroups();
        Set<Expense> groupExpenses = groups.stream().flatMap(group -> getExpensesForGroup(group.getName()).stream()).collect(Collectors.toSet());
        List<Expense> userExpenses = expenseRepository.findByMembershipUserEmail(getUserEmail());
        double groupSum = groupExpenses.stream().mapToDouble(Expense::getPrice).sum();
        double userSum = userExpenses.stream().mapToDouble(Expense::getPrice).sum();
        return new ExpInfo(userSum, groupSum);
    }

    @Override
    public Map<String, Double> getMonthlyExpensesForUser(String year, String currCode) {
        Currency currency = currencyRepository.findBySymbol(currCode);
        ExpenseFilter filter = new ExpenseFilter();
        filter.setBeginDate(LocalDate.ofYearDay(Integer.parseInt(year), 1));
        filter.setEndDate(LocalDate.ofYearDay(Integer.parseInt(year), endOfTheYear(year)));
        filter.setEmail(getUserEmail());
        List<Expense> userExpenses = searchExpenses(filter);
        return totalExpensesMap(userExpenses, e -> e.getDate().getMonth().name(), currency);

    }

    private int endOfTheYear (String year) {
        if (LocalDate.ofYearDay(Integer.parseInt(year), 1).isLeapYear()) {
            return 366;
        } else {
            return 365;
        }
    }

    @Override
    public Map<String, Double> getMonthlyExpensesForGroup(String year, String currCode) {
        Currency currency = currencyRepository.findBySymbol(currCode);
        ExpenseFilter filter = new ExpenseFilter();
        filter.setBeginDate(LocalDate.ofYearDay(Integer.parseInt(year), 1));
        filter.setEndDate(LocalDate.ofYearDay(Integer.parseInt(year), endOfTheYear(year)));
        filter.setGroupName(getGroupName());
        List<Expense> groupExpenses = searchExpenses(filter);
        return totalExpensesMap(groupExpenses, e-> e.getDate().getMonth().name(), currency);
    }

    @Override
    public Map<String, Double> getSumOfCategoryExpansesForGroup(String begin, String end, String currCode) {
        LocalDate beginDate = LocalDate.parse(begin);
        LocalDate endDate = LocalDate.parse(end);
        Currency currency = currencyRepository.findBySymbol(currCode);
        ExpenseFilter filter = new ExpenseFilter();
        filter.setBeginDate(beginDate);
        filter.setEndDate(endDate);
        List<Expense> categoryExpenses = searchExpenses(filter);
        return totalExpensesMap(categoryExpenses, e->e.getCategory().getName(), currency);
    }

    @Override
    public Map<String, Double> getSumOfCategoryExpansesForUser(String begin, String end, String currCode) {
        LocalDate beginDate = LocalDate.parse(begin);
        LocalDate endDate = LocalDate.parse(end);
        Currency currency = currencyRepository.findBySymbol(currCode);
        ExpenseFilter filter = new ExpenseFilter();
        filter.setBeginDate(beginDate);
        filter.setEndDate(endDate);
        filter.setEmail(getUserEmail());
        List<Expense> categoryExpenses = searchExpenses(filter);
        return totalExpensesMap(categoryExpenses, e->e.getCategory().getName(), currency);
    }

    @Override
    public Optional<Expense> getRecentExpense() {
        ExpenseFilter filter = new ExpenseFilter();
        filter.setEmail(getUserEmail());
        List<Expense> expenses = searchExpenses(filter);
        return expenses.stream().max(Comparator.comparing(Expense::getDate));
    }


    private String getGroupName() {
        return getAllGroups().getFirst().getName();
    }

    private List<BaseGroup> getAllGroups() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<User> user = userRepository.findByEmail(email);
        List<BaseGroup> groups = null;
        if (user.isPresent()) {
            groups = membershipRepository.findBaseGroupsByUser_Id(user.get().getId());
        }
        return groups;
    }

    @Override
    public Map<LocalDate, List<Expense>> getGroupExpenseAsDateMap(String name) {
        List<Expense> expenses = getExpensesForGroup(name);
        return expenses.stream()
                .collect(Collectors.groupingBy(Expense::getDate));
    }

    @Override
    public Map<Category, List<Expense>> getGroupExpenseAsCategoryMap(String name) {
        List<Expense> expenses = getExpensesForGroup(name);
        return expenses.stream()
                .collect(Collectors.groupingBy(Expense::getCategory));
    }

    @Override
    public List<Expense> searchExpenses(ExpenseFilter filter) {
        if (filter.getGroupName() == null || filter.getGroupName().isEmpty()) {
            filter.setGroupName(getGroupName());
        }
        if (!isGroupNameValid(filter.getGroupName())) {
            return Collections.emptyList();
        }
        Specification<Expense> spec = Specification.where(null);
        spec = spec.and(ExpenseSpecification.hasCategory(filter.getCategoryName()));
        spec = spec.and(ExpenseSpecification.dateIs(filter.getDate()));
        spec = spec.and(ExpenseSpecification.dateBetween(filter.getBeginDate(), filter.getEndDate()));
        spec = spec.and(ExpenseSpecification.priceBetween(filter.getPriceMin(), filter.getPriceMax()));
        spec = spec.and(ExpenseSpecification.hasGroup(filter.getGroupName()));
        spec = spec.and(ExpenseSpecification.isUser(filter.getEmail()));
        return expenseRepository.findAll(spec);
    }

    private boolean isGroupNameValid(String name){
        return getAllGroups().stream()
                .anyMatch(group -> group.getName().equals(name));
    }

    private String getUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    private double convertFromCurrencyToAnother(double value, Currency srcCurr, Currency dstCurr) {
        if (!srcCurr.getSymbol().equals("PLN")) {
            value *= srcCurr.getExchangeRate();
        }
        if (!dstCurr.getSymbol().equals("PLN")) {
            value /= dstCurr.getExchangeRate();
        }
        return value;
    }
    private Map<String, Double> totalExpensesMap(List<Expense> queryResult, Function<Expense, String> keyExtractor, Currency dstCurr) {
        Map<String, Double> map = new LinkedHashMap<>();
        for (Expense expense : queryResult) {
            String key = keyExtractor.apply(expense);
            double price = convertFromCurrencyToAnother(expense.getPrice(), expense.getCurrency(), dstCurr);
            map.put(key, map.getOrDefault(key, 0.0) + price);
        }
        return map;
    }
}
