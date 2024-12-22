package com.example.expenseapi;

import com.example.expenseapi.pojo.*;
import com.example.expenseapi.repository.*;
import com.example.expenseapi.utils.CurrencyRatesFetcher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class ExpenseApiApplication implements CommandLineRunner {
    final ExpenseRepository expenseRepository;
    final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final ArchivedGroupRepository archivedGroupRepository;
    private final CurrencyRepository currencyRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public ExpenseApiApplication(ExpenseRepository expenseRepository, UserRepository userRepository, CategoryRepository categoryRepository, GroupRepository groupRepository, MembershipRepository membershipRepository, ArchivedGroupRepository archivedGroupRepository, CurrencyRepository currencyRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.archivedGroupRepository = archivedGroupRepository;
        this.currencyRepository = currencyRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ExpenseApiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Group[] groups = null;
        User[] users = null;
        Category[] categories = null;
        Membership[] memberships = null;
        ArchivedGroup[] archivedGroups = null;
        Currency[] currencies = null;
        Role[] roles = null;
        if (groupRepository.count() == 0) {
            groups = new Group[]{
                    new Group("family"),
                    new Group("workers"),
            };
            groupRepository.saveAll(Arrays.asList(groups));
        }
        if (archivedGroupRepository.count() == 0) {
            archivedGroups = new ArchivedGroup[]{
                    new ArchivedGroup("family2"),
                    new ArchivedGroup("workers2"),
            };
            archivedGroupRepository.saveAll(Arrays.asList(archivedGroups));
        }
        if (userRepository.count() == 0) {
            users = new User[]{
                    new User("Herkules1", "Herkules1", "herkules1@gmail.com", passwordEncoder.encode("123")),
                    new User("Herkules2", "Herkules2", "herkules2@gmail.com", passwordEncoder.encode("234")),
                    new User("Herkules3", "Herkules3", "herkules3@gmail.com", passwordEncoder.encode("345")),
            };
            userRepository.saveAll(Arrays.asList(users));
        }
        if (roleRepository.count() == 0) {
            roles = new Role[]{
                    new Role("admin"),
                    new Role("member"),
            };
            roleRepository.saveAll(Arrays.asList(roles));
        }
        if (membershipRepository.count() == 0) {
            memberships = new Membership[]{
                    new Membership(users[0], groups[0], "family", roles[0]),
                    new Membership(users[1], groups[0], "family", roles[1]),
                    new Membership(users[2], groups[1], "workers", roles[0]),
                    new Membership(users[0], archivedGroups[0], "family2", roles[0]),
                    new Membership(users[1], archivedGroups[1], "workers2", roles[1]),
            };
            membershipRepository.saveAll(Arrays.asList(memberships));
        }
        if (categoryRepository.count() == 0) {
            categories = new Category[]{
                    new Category(),
                    new Category("Transport")
            };
            categoryRepository.saveAll((Arrays.asList(categories)));
        }
        if (currencyRepository.count() == 0) {
            currencies = new Currency[]{
                    new Currency("Zlotowka", "PLN", 1),
                    new Currency("Dollar", "USD", CurrencyRatesFetcher.getCurrencyRates("USD")),
                    new Currency("Euro", "EUR", CurrencyRatesFetcher.getCurrencyRates("EUR"))
            };
            currencyRepository.saveAll(Arrays.asList(currencies));
        }
        if (expenseRepository.count() == 0) {
            Expense[] expenses = new Expense[]{
                    new Expense(100, users[0], categories[0], LocalDate.of(2024, 11, 30), currencies[0]),
                    new Expense(200, users[1], categories[1], LocalDate.of(2024, 11, 30), currencies[1]),
                    new Expense(300, users[2], categories[0], currencies[0]),
                    new Expense(300, users[2], categories[0], currencies[2]),
            };
            expenseRepository.saveAll(Arrays.asList(expenses));
        }

    }
}
