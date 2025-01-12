package com.example.expenseapi.service;

import com.example.expenseapi.dto.CursorPageResponse;
import com.example.expenseapi.dto.ExpenseCreateDTO;
import com.example.expenseapi.dto.ExpenseDTO;
import com.example.expenseapi.exception.*;
import com.example.expenseapi.filter.ExpenseFilter;
import com.example.expenseapi.mapper.UserMapper;
import com.example.expenseapi.pojo.*;
import com.example.expenseapi.pojo.Currency;
import com.example.expenseapi.repository.*;
import com.example.expenseapi.mapper.ExpenseMapper;
import com.example.expenseapi.utils.AuthHelper;
import com.example.expenseapi.specification.ExpenseSpecification;
import com.example.expenseapi.utils.CursorPaginationUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.beans.FeatureDescriptor;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;
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

    @CacheEvict(value = {
            "expenses",
            "expensesPage",
            "expInfoGroup",
            "expInfoAllGroups",
            "expensesMap",
            "recentExpense",
            "groupExpenseDateMap",
            "groupExpenseCategoryMap",
            "searchExpensesDTO",
            "searchExpensesPagesDTO",
            "expensesUserPage"
    }, allEntries = true)
    public ExpenseDTO save(ExpenseCreateDTO expenseDTO) {
        Expense entity = expenseMapper.expenseCreateDTOToExpense(expenseDTO);
        Expense savedExpense = expenseRepository.save(entity);
        return expenseMapper.expenseToExpenseDTO(savedExpense);
    }

    @Override
    @Cacheable(value = "ExpenseID", key = "#id")
    public ExpenseDTO getMapped(Long id) {
        return expenseMapper.expenseToExpenseDTO(get(id));
    }

    @Override
    @CacheEvict(value = {
            "expenses",
            "expensesPage",
            "expInfoGroup",
            "expInfoAllGroups",
            "expensesMap",
            "recentExpense",
            "groupExpenseDateMap",
            "groupExpenseCategoryMap",
            "searchExpensesDTO",
            "searchExpensesPagesDTO",
            "expensesUserPage"
    }, allEntries = true)
    public ExpenseDTO createExpense(ExpenseCreateDTO createDTO) {
        User user = AuthHelper.getUser();
        if (!hasAllRequiredFields(createDTO)) {
            throw new RequiredFieldsMissingException();
        }
        fillDefaultsFromUserPreferences(createDTO, user);
        Expense expense = expenseMapper.expenseCreateDTOToExpense(createDTO);
        Membership membership = membershipRepository
                .findByUserIdAndGroupName(createDTO.getUser().getId(), createDTO.getGroupName())
                .orElseThrow(() -> new UserNotInGroupException(createDTO.getGroupName(), createDTO.getUser().getId()));
        expense.setMembership(membership);
        if (createDTO.getCategoryName() != null) {
            expense.setCategory(categoryRepository.findByName(createDTO.getCategoryName())
                    .orElseThrow(() -> new CategoryNotFoundException(createDTO.getCategoryName())));
        }
        if (createDTO.getExpenseDate() != null) {
            expense.setDate(createDTO.getExpenseDate());
        }
        expense.setMethod(methodOfPaymentRepository.findByName(createDTO.getMethodOfPayment())
                .orElseThrow(() -> new MethodNotFoundException(createDTO.getMethodOfPayment())));
        expense.setCurrency(currencyRepository.findBySymbol(createDTO.getCurrencyCode())
                .orElseThrow(() -> new CurrencyNotFoundException(createDTO.getCurrencyCode())));
        Expense savedExpense = expenseRepository.save(expense);
        return expenseMapper.expenseToExpenseDTO(savedExpense);
    }

    private void fillDefaultsFromUserPreferences(ExpenseCreateDTO dto, User user) {
        if (dto.getUser() == null) {
            dto.setUser(userMapper.userToUserDTO(user));
        }
        if (dto.getCurrencyCode() == null) {
            dto.setCurrencyCode(user.getPreference().getCurrency().getSymbol());
        }
        if (dto.getMethodOfPayment() == null) {
            dto.setMethodOfPayment(user.getPreference().getMethod().getName());
        }
    }

    @Override
    @Cacheable(value = "expensesPage", key = "#name + '_' + #page + '_' + #size")
    public Page<ExpenseDTO> getExpensesForGroup(String name, int page, int size) {
        return searchExpensesPagesDTO(buildGroupFilter(name), page, size);
    }

    private List<ExpenseDTO> getExpensesForGroup(String name) {
        return searchExpensesDTO(buildGroupFilter(name));
    }

    @Override
    @Cacheable(value = "expInfoGroup", key = "#group")
    public ExpInfo getExpInfo(String group) {
        List<ExpenseDTO> groupExpenses = getExpensesForGroup(group);
        List<ExpenseDTO> userExpenses = groupExpenses.stream()
                .filter(expense -> expense.getUser().getEmail().equals(AuthHelper.getUserEmail()))
                .toList();
        return calculateExpInfo(groupExpenses, userExpenses);
    }

    @Override
    @Cacheable(value = "expInfoAllGroups")
    public ExpInfo getExpInfo() {
        List<BaseGroup> groups = AuthHelper.getAllGroups();
        Set<ExpenseDTO> groupExpenses = groups.stream()
                .flatMap(group -> getExpensesForGroup(group.getName()).stream())
                .collect(Collectors.toSet());

        List<Expense> userExpenses = expenseRepository.findByMembershipUserEmail(AuthHelper.getUserEmail());
        List<ExpenseDTO> userExpensesDTO = userExpenses.stream()
                .map(expenseMapper::expenseToExpenseDTO)
                .toList();
        return calculateExpInfo(groupExpenses, userExpensesDTO);
    }

    private double sumExpensesInCurrency(Collection<ExpenseDTO> expenses, Currency targetCurrency) {
        return expenses.stream()
                .mapToDouble(expense ->
                        convertFromCurrencyToAnother(expense.getPrice(), expense.getCurrency(), targetCurrency)
                ).sum();
    }

    private ExpInfo calculateExpInfo(Collection<ExpenseDTO> groupExpenses, Collection<ExpenseDTO> userExpenses) {
        Currency currency = AuthHelper.getUser().getPreference().getCurrency();
        double groupSum = sumExpensesInCurrency(groupExpenses, currency);
        double userSum = sumExpensesInCurrency(userExpenses, currency);
        return new ExpInfo(userSum, groupSum);
    }

    @Override
    @Cacheable(value = "expensesMap", key = "#filter.toString() + '_' + #keyType")
    public Map<String, Double> getMapResult(ExpenseFilter filter,  String keyType) {
        List<ExpenseDTO> result = searchExpensesDTO(filter);
        Currency currency = AuthHelper.getUser().getPreference().getCurrency();
        Function<ExpenseDTO, String> keyExtractor = findKeyExtractor(keyType);
        if (keyExtractor == null) throw new InvalidKeyTypeException(keyType);
        return totalExpensesMap(result, keyExtractor, currency);
    }

    private ExpenseFilter buildGroupFilter(String groupName) {
        ExpenseFilter filter = new ExpenseFilter();
        filter.setGroupName(groupName);
        return filter;
    }

    @Override
    @Cacheable(value = "recentExpense", key = "#groupName")
    public Optional<ExpenseDTO> getRecentExpense(String groupName) {
        List<ExpenseDTO> expenses = searchExpensesDTO(buildGroupFilter(groupName));
        return expenses.stream().max(Comparator.comparing(ExpenseDTO::getId));
    }

    @Override
    @Cacheable(value = "groupExpenseDateMap", key = "#name + '_' + #lastId + '_' + #lastDate + '_' + #size + '_' + #desc")
    public CursorPageResponse<Map<LocalDate, List<ExpenseDTO>>> getGroupExpenseAsDateMap(String name, Long lastId, LocalDate lastDate, int size, boolean desc) {
        List<ExpenseDTO> expenses = getExpensesForGroupDateCursorBased(name, lastId, lastDate, size, desc);
        Map<LocalDate, List<ExpenseDTO>> grouped = expenses.stream()
                .collect(Collectors.groupingBy(ExpenseDTO::getExpenseDate, LinkedHashMap::new, Collectors.toList()));
        CursorPageResponse<Map<LocalDate, List<ExpenseDTO>>> response = new CursorPageResponse<>();
        response.setData(grouped);
        response.setHasMore(expenses.size() == size);
        if (!expenses.isEmpty()) {
            ExpenseDTO lastItem = expenses.getLast();
            response.setNextLastKey(lastItem.getExpenseDate().toString());
            response.setNextLastId(lastItem.getId());
        }
        return response;
    }

    private List<ExpenseDTO> getExpensesForGroupCursorBased(
            String groupName,
            Long lastId,
            Object cursorValue,
            int size,
            boolean desc,
            Function<Boolean, Sort> sortBuilder,
            BiFunction<Long, Object, Specification<Expense>> cursorSpecBuilder
    ) {
        ExpenseFilter filter = buildGroupFilter(groupName);
        Specification<Expense> spec = prepareSpecification(filter);
        if (cursorValue != null && lastId != null && lastId > 0) {
            spec = spec.and(cursorSpecBuilder.apply(lastId, cursorValue));
        }
        Sort sort = sortBuilder.apply(desc);
        Pageable pageable = PageRequest.of(0, size, sort);
        Page<Expense> pageResult = expenseRepository.findAll(spec, pageable);
        return pageResult.getContent()
                .stream()
                .map(expenseMapper::expenseToExpenseDTO)
                .toList();
    }


    public List<ExpenseDTO> getExpensesForGroupDateCursorBased(
            String name,
            Long lastId,
            LocalDate lastDate,
            int size,
            boolean desc
    ) {
        if (AuthHelper.isGroupNameInvalid(name))
            throw new UserNotInGroupException(name, AuthHelper.getUser().getId());
        return getExpensesForGroupCursorBased(
                name,
                lastId,
                lastDate,
                size,
                desc,
                CursorPaginationUtils::buildSortForDate,
                (id, dateVal) -> CursorPaginationUtils.dateCursorSpec(id, (LocalDate) dateVal, desc)
        );
    }

    @Override
    @Cacheable(value = "groupExpenseCategoryMap", key = "#name + '_' + #lastId + '_' + #lastCategory + '_' + #size + '_' + #desc")
    public CursorPageResponse<Map<Category, List<ExpenseDTO>>> getGroupExpenseAsCategoryMap(
            String name,
            Long lastId,
            String lastCategory,
            int size,
            boolean desc
    ) {
        List<ExpenseDTO> expensesList = getExpensesForGroupCategoryCursorBased(name, lastId, lastCategory, size, desc);
        Map<Category, List<ExpenseDTO>> grouped = expensesList.stream()
                .collect(Collectors.groupingBy(ExpenseDTO::getCategory, LinkedHashMap::new, Collectors.toList()));
        CursorPageResponse<Map<Category, List<ExpenseDTO>>> response = new CursorPageResponse<>();
        response.setData(grouped);
        response.setHasMore(expensesList.size() == size);
        if (!expensesList.isEmpty()) {
            ExpenseDTO lastDto = expensesList.getLast();
            response.setNextLastKey(lastDto.getCategory().getName());
            response.setNextLastId(lastDto.getId());
        }

        return response;
    }

    public List<ExpenseDTO> getExpensesForGroupCategoryCursorBased(
            String name,
            Long lastId,
            String lastCategory,
            int size,
            boolean desc
    ) {
        if (AuthHelper.isGroupNameInvalid(name))
            throw new UserNotInGroupException(name, AuthHelper.getUser().getId());
        return getExpensesForGroupCursorBased(
                name,
                lastId,
                lastCategory,
                size,
                desc,
                CursorPaginationUtils::buildSortForCategory,
                (id, catVal) -> CursorPaginationUtils.categoryCursorSpec(id, (String) catVal, desc)
        );
    }

    private List<ExpenseDTO> searchExpensesDTO(ExpenseFilter filter) {
        handleGroupFilter(filter);
        Specification<Expense> spec = prepareSpecification(filter);
        return expenseRepository.findAll(spec).stream()
                .map(expenseMapper::expenseToExpenseDTO)
                .toList();
    }

    @Override
    @Cacheable(value = "searchExpensesPagesDTO", key = "#filter.toString() + '_' + #page + '_' + #size")
    public Page<ExpenseDTO> searchExpensesPagesDTO(ExpenseFilter filter, int page, int size) {
        handleGroupFilter(filter);
        Specification<Expense> spec = prepareSpecification(filter);
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return expenseRepository.findAll(spec, pageable)
                .map(expenseMapper::expenseToExpenseDTO);
    }

    private void handleGroupFilter(ExpenseFilter filter) {
        if (filter.getGroupName() == null || filter.getGroupName().isEmpty()) {
            filter.setGroupName(AuthHelper.getGroupName());
        }
        if (AuthHelper.isGroupNameInvalid(filter.getGroupName())) {
            throw new UserNotInGroupException(filter.getGroupName(), AuthHelper.getUser().getId());
        }
    }

    private Specification<Expense> prepareSpecification(ExpenseFilter filter) {
        Specification<Expense> spec = Specification.where(null);
        spec = spec.and(ExpenseSpecification.hasCategory(filter.getCategoryNames()));
        spec = spec.and(ExpenseSpecification.dateIs(filter.getDate()));
        spec = spec.and(ExpenseSpecification.dateBetween(filter.getBeginDate(), filter.getEndDate()));
        spec = spec.and(ExpenseSpecification.priceBetween(filter.getPriceMin(), filter.getPriceMax()));
        spec = spec.and(ExpenseSpecification.hasGroup(filter.getGroupName()));
        spec = spec.and(ExpenseSpecification.isUser(filter.getEmail()));
        spec = spec.and(ExpenseSpecification.isUserInList(filter.getEmails()));
        spec = spec.and(ExpenseSpecification.hasMethod(filter.getMethodsOfPayment()));
        return spec;
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

    @Caching(evict = {
            @CacheEvict(value = {
                    "expensesPage",
                    "expInfoGroup",
                    "expInfoAllGroups",
                    "expensesMap",
                    "recentExpense",
                    "groupExpenseDateMap",
                    "groupExpenseCategoryMap",
                    "searchExpensesDTO",
                    "searchExpensesPagesDTO",
                    "expensesUserPage"
            }, allEntries = true),
            @CacheEvict(value = "ExpenseID", key = "#id")
    })
    public ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));
        Expense updatedExpense = expenseMapper.expenseDTOToExpense(expenseDTO);
        BeanUtils.copyProperties(updatedExpense, expense, getNullPropertyNames(updatedExpense));
        expense.setMethod(methodOfPaymentRepository.findByName(updatedExpense.getMethod().getName())
                .orElseThrow(() -> new MethodNotFoundException(updatedExpense.getMethod().getName())));
        return expenseMapper.expenseToExpenseDTO(expenseRepository.save(expense));
    }

    private boolean hasAllRequiredFields(ExpenseCreateDTO expenseCreateDTO) {
        return expenseCreateDTO.getTitle() != null &&
                expenseCreateDTO.getPrice() != null &&
                expenseCreateDTO.getCategoryName() != null &&
                expenseCreateDTO.getGroupName() != null;

    }

    @Override
    @Cacheable(value = "expensesUserPage", key = "#page + '_' + #size + '_' + T(com.example.expenseapi.utils.AuthHelper).getUserEmail()")
    public Page<ExpenseDTO> getExpensesForUser(int page, int size) {
        ExpenseFilter filter = new ExpenseFilter();
        filter.setEmail(AuthHelper.getUserEmail());
        Specification<Expense> spec = prepareSpecification(filter);
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return expenseRepository.findAll(spec, pageable)
                .map(expenseMapper::expenseToExpenseDTO);
    }

    @Caching(evict = {
            @CacheEvict(value = {
                    "expensesPage",
                    "expInfoGroup",
                    "expInfoAllGroups",
                    "expensesMap",
                    "recentExpense",
                    "groupExpenseDateMap",
                    "groupExpenseCategoryMap",
                    "searchExpensesDTO",
                    "searchExpensesPagesDTO",
                    "expensesUserPage"
            }, allEntries = true),
            @CacheEvict(value = "ExpenseID", key = "#id")
    })
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @CacheEvict(value = {
            "expensesPage",
            "expInfoGroup",
            "expInfoAllGroups",
            "expensesMap",
            "recentExpense",
            "groupExpenseDateMap",
            "groupExpenseCategoryMap",
            "searchExpensesDTO",
            "searchExpensesPagesDTO",
            "expensesUserPage",
            "ExpenseID"
    }, allEntries = true)
    public void deleteAllData() {
        super.deleteAllData();
    }

    @Override
    @CacheEvict(value = {
            "expensesPage",
            "expInfoGroup",
            "expInfoAllGroups",
            "expensesMap",
            "recentExpense",
            "groupExpenseDateMap",
            "groupExpenseCategoryMap",
            "searchExpensesDTO",
            "searchExpensesPagesDTO",
            "expensesUserPage",
            "ExpenseID"
    }, allEntries = true)
    @Transactional
    public void deleteAllExpensesForUserId(Long id) {
        expenseRepository.deleteAllByMembershipUserId(id);
    }

    @Override
    @CacheEvict(value = {
            "expensesPage",
            "expInfoGroup",
            "expInfoAllGroups",
            "expensesMap",
            "recentExpense",
            "groupExpenseDateMap",
            "groupExpenseCategoryMap",
            "searchExpensesDTO",
            "searchExpensesPagesDTO",
            "expensesUserPage",
            "ExpenseID"
    }, allEntries = true)
    @Transactional
    public void deleteAllExpensesForUserIdAndGroupName(Long userId, String groupName) {
        expenseRepository.deleteAllByMembership_User_IdAndMembership_Group_Name(userId, groupName);
    }
}

