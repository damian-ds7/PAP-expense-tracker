package com.example.expenseapi.service;

import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {
    public UserServiceImpl(UserRepository repository) {
        super(repository);
    }
}
