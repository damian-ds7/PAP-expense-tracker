package com.example.expenseapi.service;

import com.example.expenseapi.dto.UserDTO;
import com.example.expenseapi.filter.UserFilter;
import com.example.expenseapi.mapper.UserMapper;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.UserRepository;
import com.example.expenseapi.utils.UserSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository repository, UserRepository userRepository, UserMapper userMapper) {
        super(repository);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDTO> searchUsersDTO(UserFilter filter) {
        Specification<User> spec = Specification.where(null);
        spec = spec.and(UserSpecification.nameContains(filter.getName()));
        spec = spec.and(UserSpecification.surnameContains(filter.getSurname()));
        return userRepository.findAll(spec).stream()
                .map(userMapper::userToUserDTO)
                .toList();
    }
}

