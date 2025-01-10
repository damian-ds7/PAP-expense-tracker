package com.example.expenseapi.utils;

import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@Profile("test")
public class EncodePasswords {
    @Bean
    public CommandLineRunner updatePasswords(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            List<User> allUsers = userRepository.findAll();
            allUsers.forEach(user ->  {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
                });
            };
        }
    }

