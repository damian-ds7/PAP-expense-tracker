package com.example.expenseapi.dto;

import com.example.expenseapi.pojo.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MembershipCreateDTO {
    private UserDTO user;
    private Group group;
}
