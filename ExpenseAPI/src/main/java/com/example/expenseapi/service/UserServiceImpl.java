package com.example.expenseapi.service;

import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
