package com.example.expenseapi.service;

import com.example.expenseapi.dto.ExpenseCreateDTO;
import com.example.expenseapi.dto.ExpenseDTO;
import com.example.expenseapi.dto.ExpenseFilter;
import com.example.expenseapi.pojo.*;
import com.example.expenseapi.pojo.Currency;
import com.example.expenseapi.repository.*;
import com.example.expenseapi.utils.ExpenseMapper;
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
    private final ExpenseMapper expenseMapper;

    public ExpenseServiceImpl(ExpenseRepository repository, CategoryRepository categoryRepository, CurrencyRepository currencyRepository, UserRepository userRepository, MembershipRepository membershipRepository, MethodOfPaymentRepository methodOfPaymentRepository, ExpenseMapper expenseMapper) {
        super(repository);
        this.expenseRepository = repository;
        this.categoryRepository = categoryRepository;
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
        this.methodOfPaymentRepository = methodOfPaymentRepository;
        this.expenseMapper = expenseMapper;
    }

    public ExpenseDTO save(ExpenseCreateDTO expenseDTO) {
        Expense entity = expenseMapper.expenseCreateDTOToExpense(expenseDTO);
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
        Expense savedExpense = expenseRepository.save(entity);
        return expenseMapper.expenseToExpenseDTO(savedExpense);
    }

    @Override
    public ExpenseDTO createExpense(ExpenseCreateDTO createDTO) {
        Expense expense = expenseMapper.expenseCreateDTOToExpense(createDTO);
        Optional<Membership> membershipOpt = membershipRepository.findByUserIdAndGroupName(createDTO.getUser().getId(), createDTO.getGroupName());
        if (membershipOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid membership ID");
        }
        expense.setMembership(membershipOpt.get());
        if (createDTO.getCategoryName() != null) {
            Category category = categoryRepository.findByName(createDTO.getCategoryName());
            expense.setCategory(category);
        }
        if (createDTO.getMethodOfPayment() != null) {
            MethodOfPayment method = methodOfPaymentRepository.findByName(createDTO.getMethodOfPayment());
            expense.setMethod(method);
        }
        if (createDTO.getCurrencyCode() != null) {
            Currency currency = currencyRepository.findBySymbol(createDTO.getCurrencyCode());
            expense.setCurrency(currency);
        }
        if (createDTO.getExpenseDate() != null) {
            expense.setDate(createDTO.getExpenseDate());
        }
        Expense savedExpense = expenseRepository.save(expense);
        return expenseMapper.expenseToExpenseDTO(savedExpense);
    }

    @Override
    public List<ExpenseDTO> getExpensesForGroup(String name) {
        ExpenseFilter filter = new ExpenseFilter();
        filter.setGroupName(name);
        return searchExpensesDTO(filter);
    }

    @Override
    public List<ExpenseDTO> getExpensesForUser() {
        ExpenseFilter filter = new ExpenseFilter();
        filter.setEmail(getUserEmail());
        return searchExpensesDTO(filter);
    }

    @Override
    public ExpInfo getExpInfo(String group) {
        List<ExpenseDTO> groupExpenses = getExpensesForGroup(group);
        List<ExpenseDTO> userExpenses = groupExpenses.stream()
                .filter(expense -> expense.getUser().getEmail().equals(getUserEmail()))
                .toList(); // Returns only user's expenses in provided group, not in all groups
        double groupSum = groupExpenses.stream().mapToDouble(ExpenseDTO::getPrice).sum();
        double userSum = userExpenses.stream().mapToDouble(ExpenseDTO::getPrice).sum();
        return new ExpInfo(userSum, groupSum);
    }

    @Override
    public ExpInfo getExpInfo() {
        List<BaseGroup> groups = getAllGroups();
        Set<ExpenseDTO> groupExpenses = groups.stream().flatMap(group -> getExpensesForGroup(group.getName()).stream()).collect(Collectors.toSet());
        List<Expense> userExpenses = expenseRepository.findByMembershipUserEmail(getUserEmail());
        double groupSum = groupExpenses.stream().mapToDouble(ExpenseDTO::getPrice).sum();
        double userSum = userExpenses.stream().mapToDouble(Expense::getPrice).sum();
        return new ExpInfo(userSum, groupSum);
    }

    @Override
    public Map<String, Double> getMapResult(ExpenseFilter filter, String currCode, String keyType) {
        List<ExpenseDTO> result = searchExpensesDTO(filter);
        Currency currency = currencyRepository.findBySymbol(currCode);
        Function<ExpenseDTO, String> keyExtractor = findKeyExtractor(keyType);
        if (keyExtractor == null) throw new IllegalArgumentException("Invalid key type");
        return totalExpensesMap(result, keyExtractor, currency);
    }

    @Override
    public Optional<ExpenseDTO> getRecentExpense(String groupName) {
        ExpenseFilter filter = new ExpenseFilter();
        filter.setGroupName(groupName);
        List<ExpenseDTO> expenses = searchExpensesDTO(filter);
        return expenses.stream().max(Comparator.comparing(ExpenseDTO::getExpenseDate));
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
    public Map<LocalDate, List<ExpenseDTO>> getGroupExpenseAsDateMap(String name) {
        List<ExpenseDTO> expenses = getExpensesForGroup(name);
        return expenses.stream()
                .collect(Collectors.groupingBy(ExpenseDTO::getExpenseDate));
    }

    @Override
    public Map<Category, List<ExpenseDTO>> getGroupExpenseAsCategoryMap(String name) {
        List<ExpenseDTO> expenses = getExpensesForGroup(name);
        return expenses.stream()
                .collect(Collectors.groupingBy(ExpenseDTO::getCategory));
    }

    @Override
    public List<ExpenseDTO> searchExpensesDTO(ExpenseFilter filter) {
        if (filter.getGroupName() == null || filter.getGroupName().isEmpty()) {
            filter.setGroupName(getGroupName());
        }
        if (!isGroupNameValid(filter.getGroupName())) {
            return Collections.emptyList();
        }
        Specification<Expense> spec = Specification.where(null);
        spec = spec.and(ExpenseSpecification.hasCategory(filter.getCategoryNames()));
        spec = spec.and(ExpenseSpecification.dateIs(filter.getDate()));
        spec = spec.and(ExpenseSpecification.dateBetween(filter.getBeginDate(), filter.getEndDate()));
        spec = spec.and(ExpenseSpecification.priceBetween(filter.getPriceMin(), filter.getPriceMax()));
        spec = spec.and(ExpenseSpecification.hasGroup(filter.getGroupName()));
        spec = spec.and(ExpenseSpecification.isUser(filter.getEmail()));
        spec = spec.and(ExpenseSpecification.isUserInList(filter.getEmails()));
        spec = spec.and(ExpenseSpecification.hasMethod(filter.getMethodsOfPayment()));
        return expenseRepository.findAll(spec).stream()
                .map(expenseMapper::expenseToExpenseDTO)
                .collect(Collectors.toList());
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

    private Function<ExpenseDTO, String> findKeyExtractor(String keyType) {
        return switch (keyType) {
            case "category" -> e -> e.getCategory().getName();
            case "method" -> ExpenseDTO::getMethodOfPayment;
            case "user" -> e -> e.getUser().getEmail();
            case "month" -> e -> e.getExpenseDate().getMonth().name();
            case "year" -> e -> String.valueOf(e.getExpenseDate().getYear());
            default -> null;
        };
    }

    private Map<String, Double> totalExpensesMap(List<ExpenseDTO> queryResult, Function<ExpenseDTO, String> keyExtractor, Currency dstCurr) {
        Map<String, Double> map = new LinkedHashMap<>();
        for (ExpenseDTO expense : queryResult) {
            String key = keyExtractor.apply(expense);
            double price = convertFromCurrencyToAnother(expense.getPrice(), expense.getCurrency(), dstCurr);
            map.put(key, map.getOrDefault(key, 0.0) + price);
        }
        return map;
    }
}
