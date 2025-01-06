package com.example.expenseapi.service;

import com.example.expenseapi.dto.ChangePasswordDTO;
import com.example.expenseapi.dto.UserDTO;
import com.example.expenseapi.dto.UserUpdateDTO;
import com.example.expenseapi.filter.UserFilter;
import com.example.expenseapi.pojo.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends GenericService<User, Long> {
    Optional<User> findByEmail(String email);
    List<UserDTO> searchUsersDTO(UserFilter filter, String groupName);

    UserDTO changePassword(ChangePasswordDTO passwordDTO);

    UserDTO update(UserUpdateDTO userUpdateDTO);
}