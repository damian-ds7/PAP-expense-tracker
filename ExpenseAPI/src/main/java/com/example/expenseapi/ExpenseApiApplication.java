package com.example.expenseapi;

import com.example.expenseapi.pojo.Expense;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.ExpenseRepository;
import com.example.expenseapi.repository.UserRepository;
import com.example.expenseapi.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class ExpenseApiApplication implements CommandLineRunner {
    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    UserRepository userRepository;
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
        Expense[] expenses = new Expense[]{
                new Expense(100),
                new Expense(200),
                new Expense(300),
        };
        expenseRepository.saveAll(Arrays.asList(expenses));

    }
}
