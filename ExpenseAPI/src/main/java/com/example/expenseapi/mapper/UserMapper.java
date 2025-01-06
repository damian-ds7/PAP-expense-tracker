package com.example.expenseapi.mapper;

import com.example.expenseapi.dto.UserDTO;
import com.example.expenseapi.pojo.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    @Mapping(source = "email", target = "email")
    UserDTO userToUserDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    @Mapping(source = "email", target = "email")
    User UserDTOToUser(UserDTO userDTO);
}
