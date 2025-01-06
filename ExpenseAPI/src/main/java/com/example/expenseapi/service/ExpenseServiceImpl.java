package com.example.expenseapi.service;

import com.example.expenseapi.dto.ExpenseCreateDTO;
import com.example.expenseapi.dto.ExpenseDTO;
import com.example.expenseapi.exception.BadRequestException;
import com.example.expenseapi.exception.ForbiddenException;
import com.example.expenseapi.filter.ExpenseFilter;
import com.example.expenseapi.mapper.UserMapper;
import com.example.expenseapi.pojo.*;
import com.example.expenseapi.pojo.Currency;
import com.example.expenseapi.repository.*;
import com.example.expenseapi.mapper.ExpenseMapper;
import com.example.expenseapi.utils.AuthHelper;
import com.example.expenseapi.specification.ExpenseSpecification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.beans.FeatureDescriptor;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

@Service
public class ExpenseServiceImpl extends GenericServiceImpl<Expense, Long> implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final CurrencyRepository currencyRepository;
    private final MembershipRepository membershipRepository;
    private final MethodOfPaymentRepository methodOfPaymentRepository;
    private final ExpenseMapper expenseMapper;
    private final UserMapper userMapper;

    public ExpenseServiceImpl(ExpenseRepository repository, CategoryRepository categoryRepository, CurrencyRepository currencyRepository, MembershipRepository membershipRepository, MethodOfPaymentRepository methodOfPaymentRepository, ExpenseMapper expenseMapper, UserMapper userMapper) {
        super(repository);
        this.expenseRepository = repository;
        this.categoryRepository = categoryRepository;
        this.currencyRepository = currencyRepository;
        this.membershipRepository = membershipRepository;
        this.methodOfPaymentRepository = methodOfPaymentRepository;
        this.expenseMapper = expenseMapper;
        this.userMapper = userMapper;
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
        User user = AuthHelper.getUser();
        if (areNeededFieldsProvided(createDTO)) {
            throw new BadRequestException("Title, price, category name and group name are required");
        }
        if (createDTO.getUser() == null) {
            createDTO.setUser(userMapper.userToUserDTO(user));
        }
        if (createDTO.getCurrencyCode() == null) {
            createDTO.setCurrencyCode(user.getPreference().getCurrency().getSymbol());
        }
        if (createDTO.getMethodOfPayment() == null) {
            createDTO.setMethodOfPayment(user.getPreference().getMethod().getName());
        }
        Expense expense = expenseMapper.expenseCreateDTOToExpense(createDTO);
        Optional<Membership> membershipOpt = membershipRepository.findByUserIdAndGroupName(createDTO.getUser().getId(), createDTO.getGroupName());
        if (membershipOpt.isEmpty()) {
            throw new ForbiddenException("User is not a member of the group");
        }
        expense.setMembership(membershipOpt.get());
        if (createDTO.getCategoryName() != null) {
            Category category = categoryRepository.findByName(createDTO.getCategoryName());
            expense.setCategory(category);
        }
        if (createDTO.getExpenseDate() != null) {
            expense.setDate(createDTO.getExpenseDate());
        }
        expense.setMethod(methodOfPaymentRepository.findByName(createDTO.getMethodOfPayment()));
        expense.setCurrency(currencyRepository.findBySymbol(createDTO.getCurrencyCode()));
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
        filter.setEmail(AuthHelper.getUserEmail());
        return searchExpensesDTO(filter);
    }

    @Override
    public ExpInfo getExpInfo(String group) {
        List<ExpenseDTO> groupExpenses = getExpensesForGroup(group);
        List<ExpenseDTO> userExpenses = groupExpenses.stream()
                .filter(expense -> expense.getUser().getEmail().equals(AuthHelper.getUserEmail()))
                .toList(); // Returns only user's expenses in provided group, not in all groups
        double groupSum = groupExpenses.stream().mapToDouble(ExpenseDTO::getPrice).sum();
        double userSum = userExpenses.stream().mapToDouble(ExpenseDTO::getPrice).sum();
        return new ExpInfo(userSum, groupSum);
    }

    @Override
    public ExpInfo getExpInfo() {
        List<BaseGroup> groups = AuthHelper.getAllGroups();
        Set<ExpenseDTO> groupExpenses = groups.stream().flatMap(group -> getExpensesForGroup(group.getName()).stream()).collect(Collectors.toSet());
        List<Expense> userExpenses = expenseRepository.findByMembershipUserEmail(AuthHelper.getUserEmail());
        double groupSum = groupExpenses.stream().mapToDouble(ExpenseDTO::getPrice).sum();
        double userSum = userExpenses.stream().mapToDouble(Expense::getPrice).sum();
        return new ExpInfo(userSum, groupSum);
    }

    @Override
    public Map<String, Double> getMapResult(ExpenseFilter filter,  String keyType) {
        List<ExpenseDTO> result = searchExpensesDTO(filter);
        Currency currency = currencyRepository.findBySymbol(AuthHelper.getUser().getPreference().getCurrency().getSymbol());
        Function<ExpenseDTO, String> keyExtractor = findKeyExtractor(keyType);
        if (keyExtractor == null) throw new BadRequestException("Invalid key type");
        return totalExpensesMap(result, keyExtractor, currency);
    }

    @Override
    public Optional<ExpenseDTO> getRecentExpense(String groupName) {
        ExpenseFilter filter = new ExpenseFilter();
        filter.setGroupName(groupName);
        List<ExpenseDTO> expenses = searchExpensesDTO(filter);
        return expenses.stream().max(Comparator.comparing(ExpenseDTO::getId));
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
            filter.setGroupName(AuthHelper.getGroupName());
        }
        if (!AuthHelper.isGroupNameValid(filter.getGroupName())) {
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
                .toList();
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

    @Override
    public List<String> getPatternKeys() {
        return Arrays.asList("category", "method", "user");
    }

    private Function<ExpenseDTO, String> findKeyExtractor(String keyType) {
        return switch (keyType) {
            case "category" -> e -> e.getCategory().getName();
            case "method" -> e-> e.getMethodOfPayment().getName();
            case "user" -> e -> e.getUser().getEmail();
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

    private static String[] getNullPropertyNames(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        return Arrays.stream(beanWrapper.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> beanWrapper.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

    @Override
    public ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO) {
        Expense expense = expenseRepository.findById(id).orElse(null);
        Expense updatedExpense = expenseMapper.expenseDTOToExpense(expenseDTO);
        BeanUtils.copyProperties(updatedExpense, expense, getNullPropertyNames(updatedExpense));
        expense.setMethod(methodOfPaymentRepository.findByName(updatedExpense.getMethod().getName()));
        return expenseMapper.expenseToExpenseDTO(expenseRepository.save(expense));
    }

    private boolean areNeededFieldsProvided(ExpenseCreateDTO expenseCreateDTO) {
        return expenseCreateDTO.getTitle() == null ||
                expenseCreateDTO.getPrice() == null ||
                expenseCreateDTO.getCategoryName() == null ||
                expenseCreateDTO.getGroupName() == null;

    }
}
