package com.example.expenseapi;

import com.example.expenseapi.pojo.Expense;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.pojo.Category;
import com.example.expenseapi.repository.ExpenseRepository;
import com.example.expenseapi.repository.UserRepository;
import com.example.expenseapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class ExpenseApiApplication implements CommandLineRunner {
    final ExpenseRepository expenseRepository;
    final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseApiApplication(ExpenseRepository expenseRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ExpenseApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User[] users = new User[]{
            new User("Herkules1", "Herkules1", "herkules1@gmail.com"),
            new User("Herkules2", "Herkules2", "herkules2@gmail.com"),
            new User("Herkules3", "Herkules3", "herkules3@gmail.com")
        };
        userRepository.saveAll(Arrays.asList(users));

        Category[] categories = new Category[]{
                new Category(),
                new Category()
        };
        categoryRepository.saveAll((Arrays.asList(categories)));

        Expense[] expenses = new Expense[]{
                new Expense(100, users[0], categories[0]),
                new Expense(200, users[1], categories[1]),
                new Expense(300, users[2], categories[0]),
        };
        expenseRepository.saveAll(Arrays.asList(expenses));

    }
}
