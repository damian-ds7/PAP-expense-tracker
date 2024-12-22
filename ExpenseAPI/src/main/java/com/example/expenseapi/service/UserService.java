package com.example.expenseapi.service;

import com.example.expenseapi.pojo.User;

import java.util.Optional;

public interface UserService extends GenericService<User, Long> {
    Optional<User> findByEmail(String email);
}