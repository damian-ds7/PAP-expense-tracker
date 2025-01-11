package com.example.expenseapi.service;

import com.example.expenseapi.dto.UserDTO;
import com.example.expenseapi.pojo.BaseGroup;
import com.example.expenseapi.pojo.Group;
import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.User;

import java.util.List;

public interface MembershipService extends GenericService<Membership, Long> {
    List<BaseGroup> getBaseGroupsByUserId(Long userId);
    List<Membership> getMembershipsByUserId(Long userId);
    List<UserDTO> findAdmins(String group);
    List<UserDTO> findUsers(String group);
    String getRole(User user, Group group);

    Boolean isAdmin(String groupName);

    void deleteAllMembershipsForUserId(Long id);
}
