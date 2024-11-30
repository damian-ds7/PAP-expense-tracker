package com.example.expenseapi.service;

import com.example.expenseapi.pojo.User;

import java.util.List;

public interface UserService {
    User getUser(Long id);
    List<User> getAllUsers();
    User saveUser(User user);
    void deleteUser(Long id);
}
