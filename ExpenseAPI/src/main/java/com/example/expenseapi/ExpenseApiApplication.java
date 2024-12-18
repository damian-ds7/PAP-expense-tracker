package com.example.expenseapi;

import com.example.expenseapi.pojo.*;
import com.example.expenseapi.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

    public ExpenseApiApplication(ExpenseRepository expenseRepository, UserRepository userRepository, CategoryRepository categoryRepository, GroupRepository groupRepository, MembershipRepository membershipRepository, ArchivedGroupRepository archivedGroupRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.archivedGroupRepository = archivedGroupRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ExpenseApiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Group[] groups = new Group[]{
                new Group("family"),
                new Group("workers"),
        };

        groupRepository.saveAll(Arrays.asList(groups));
        ArchivedGroup[] archivedGroups = new ArchivedGroup[]{
                new ArchivedGroup("family2"),
                new ArchivedGroup("workers2"),
        };
        archivedGroupRepository.saveAll(Arrays.asList(archivedGroups));
        User[] users = new User[]{
            new User("Herkules1", "Herkules1", "herkules1@gmail.com"),
            new User("Herkules2", "Herkules2", "herkules2@gmail.com"),
            new User("Herkules3", "Herkules3", "herkules3@gmail.com" ),
        };
        userRepository.saveAll(Arrays.asList(users));
        Membership[] memberships = new Membership[]{
                new Membership(users[0], groups[0], "family"),
                new Membership(users[1], groups[0], "family"),
                new Membership(users[2], groups[1], "workers"),
                new Membership(users[0], archivedGroups[0], "family2"),
                new Membership(users[1], archivedGroups[1], "workers2"),
        };
        membershipRepository.saveAll(Arrays.asList(memberships));
        Category[] categories = new Category[]{
                new Category(),
                new Category("Transport")
        };
        categoryRepository.saveAll((Arrays.asList(categories)));

        Expense[] expenses = new Expense[]{
                new Expense(100, users[0], categories[0], LocalDate.of(2024, 11, 30)),
                new Expense(200, users[1], categories[1], LocalDate.of(2024, 11, 30)),
                new Expense(300, users[2], categories[0]),
                new Expense(300, users[2], categories[0]),
        };
        expenseRepository.saveAll(Arrays.asList(expenses));

    }
}
