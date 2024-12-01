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
    private final UserGroupRepository groupRepository;

    public ExpenseApiApplication(ExpenseRepository expenseRepository, UserRepository userRepository, CategoryRepository categoryRepository, UserGroupRepository groupRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.groupRepository = groupRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ExpenseApiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        UserGroup[] groups = new UserGroup[]{
                new UserGroup("family"),
                new UserGroup("workers"),
        };
        groupRepository.saveAll(Arrays.asList(groups));
        User[] users = new User[]{
            new User("Herkules1", "Herkules1", "herkules1@gmail.com", groups[0]),
            new User("Herkules2", "Herkules2", "herkules2@gmail.com", groups[1]),
            new User("Herkules3", "Herkules3", "herkules3@gmail.com" , groups[0]),
        };
        userRepository.saveAll(Arrays.asList(users));

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
