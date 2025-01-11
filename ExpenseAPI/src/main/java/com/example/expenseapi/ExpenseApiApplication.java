package com.example.expenseapi;

import com.example.expenseapi.pojo.*;
import com.example.expenseapi.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ExpenseApiApplication implements CommandLineRunner {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final ArchivedGroupRepository archivedGroupRepository;
    private final CurrencyRepository currencyRepository;
    private final PasswordEncoder passwordEncoder;
    private final MethodOfPaymentRepository methodOfPaymentRepository;
    private final RoleRepository roleRepository;
    private final PreferenceRepository preferenceRepository;

    public ExpenseApiApplication(
            ExpenseRepository expenseRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            GroupRepository groupRepository,
            MembershipRepository membershipRepository,
            ArchivedGroupRepository archivedGroupRepository,
            CurrencyRepository currencyRepository,
            PasswordEncoder passwordEncoder,
            MethodOfPaymentRepository methodOfPaymentRepository,
            RoleRepository roleRepository,
            PreferenceRepository preferenceRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.archivedGroupRepository = archivedGroupRepository;
        this.currencyRepository = currencyRepository;
        this.passwordEncoder = passwordEncoder;
        this.methodOfPaymentRepository = methodOfPaymentRepository;
        this.roleRepository = roleRepository;
        this.preferenceRepository = preferenceRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ExpenseApiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        initMethodOfPayments();
        initCurrencies();
        initGroups();
        initArchivedGroups();
        initPreferences();
        initUsers();
        initRoles();
        initMemberships();
        initCategories();
        initExpenses();
    }

    private void initMethodOfPayments() {
        if (methodOfPaymentRepository.count() == 0) {
            List<MethodOfPayment> methods = Arrays.asList(
                    new MethodOfPayment("cash"),
                    new MethodOfPayment("debit card")
            );
            methodOfPaymentRepository.saveAll(methods);
        }
    }

    private void initCurrencies() {
        if (currencyRepository.count() == 0) {
            List<Currency> currencies = Arrays.asList(
                    new Currency("PLN", 1.0),
                    new Currency("USD", 4.0),
                    new Currency("EUR", 5.0)
            );
            currencyRepository.saveAll(currencies);
        }
    }

    private void initGroups() {
        if (groupRepository.count() == 0) {
            List<Group> groups = Arrays.asList(
                    new Group("family"),
                    new Group("workers")
            );
            groupRepository.saveAll(groups);
        }
    }

    private void initArchivedGroups() {
        if (archivedGroupRepository.count() == 0) {
            List<ArchivedGroup> archivedGroups = Arrays.asList(
                    new ArchivedGroup("family2"),
                    new ArchivedGroup("workers2")
            );
            archivedGroupRepository.saveAll(archivedGroups);
        }
    }

    private void initPreferences() {
        if (preferenceRepository.count() == 0) {
            Currency pln = currencyRepository.findBySymbol("PLN").orElseThrow();
            Currency usd = currencyRepository.findBySymbol("USD").orElseThrow();
            Currency eur = currencyRepository.findBySymbol("EUR").orElseThrow();

            MethodOfPayment cash = methodOfPaymentRepository.findByName("cash").orElseThrow();
            MethodOfPayment card = methodOfPaymentRepository.findByName("debit card").orElseThrow();

            List<Preference> preferences = Arrays.asList(
                    new Preference(pln, cash),
                    new Preference(usd, card),
                    new Preference(eur, cash),
                    new Preference(pln, card)
            );
            preferenceRepository.saveAll(preferences);
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            List<Preference> allPreferences = preferenceRepository.findAll();
            if (allPreferences.size() < 4) {
                return;
            }
            User u1 = new User("Herkules1", "Herkules1", "herkules1@gmail.com", allPreferences.get(0), passwordEncoder.encode("123"));
            User u2 = new User("Herkules2", "Herkules2", "herkules2@gmail.com", allPreferences.get(1), passwordEncoder.encode("234"));
            User u3 = new User("Herkules3", "Herkules3", "herkules3@gmail.com", allPreferences.get(2), passwordEncoder.encode("345"));
            User u4 = new User("Herkules4", "Herkules4", "herkules4@gmail.com", allPreferences.get(3), passwordEncoder.encode("456"));

            userRepository.saveAll(Arrays.asList(u1, u2, u3, u4));
        }
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            List<Role> roles = Arrays.asList(
                    new Role("admin"),
                    new Role("member")
            );
            roleRepository.saveAll(roles);
        }
    }

    private void initMemberships() {
        if (membershipRepository.count() == 0) {
            List<User> users = userRepository.findAll();
            List<Group> groups = groupRepository.findAll();
            List<Role> roles = roleRepository.findAll();
            if (users.size() < 3 || groups.size() < 2 || roles.size() < 2) {
                return;
            }
            Membership m1 = new Membership(users.get(0), groups.get(0), roles.get(0));
            Membership m2 = new Membership(users.get(1), groups.get(0), roles.get(0));
            Membership m3 = new Membership(users.get(2), groups.get(1), roles.get(1));
            Membership m4 = new Membership(users.get(0), groups.get(1), roles.get(1));
            Membership m5 = new Membership(users.get(1), groups.get(1), roles.get(1));

            membershipRepository.saveAll(Arrays.asList(m1, m2, m3, m4, m5));
        }
    }

    private void initCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = Arrays.asList(
                    new Category(),
                    new Category("transport")
            );
            categoryRepository.saveAll(categories);
        }
    }

    private void initExpenses() {
        if (expenseRepository.count() == 0) {
            List<Membership> memberships = membershipRepository.findAll();
            List<Category> categories = categoryRepository.findAll();
            List<Currency> currencies = currencyRepository.findAll();
            List<MethodOfPayment> methods = methodOfPaymentRepository.findAll();

            if (memberships.size() < 3 || categories.isEmpty()
                    || currencies.isEmpty() || methods.isEmpty()) {
                return;
            }

            Expense e1 = new Expense("lunch", 50, memberships.get(0), categories.get(0),
                    LocalDate.of(2024, 10, 10), currencies.get(0), methods.get(0));
            Expense e2 = new Expense("dinner", 100, memberships.get(0), categories.get(0),
                    LocalDate.of(2025, 11, 30), currencies.get(0), methods.get(0));
            Expense e3 = new Expense("train-ticket", 200, memberships.get(1), categories.get(1),
                    LocalDate.of(2024, 12, 22), currencies.get(1), methods.get(1));
            Expense e4 = new Expense("groceries", 300, memberships.get(2), categories.get(0),
                    currencies.get(0), methods.get(0));
            Expense e5 = new Expense("fast-food", 300, memberships.get(2), categories.get(0),
                    currencies.get(2), methods.get(1));

            expenseRepository.saveAll(Arrays.asList(e1, e2, e3, e4, e5));
        }
    }
}
