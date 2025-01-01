package com.example.expenseapi.service;

import com.example.expenseapi.pojo.*;

import java.util.List;

public interface MembershipService extends GenericService<Membership, Long> {
    List<BaseGroup> getBaseGroupsByUserId(Long userId);
    List<Group> getGroupsByUserId(Long userId);
    List<Membership> getMembershipsByUserId(Long userId);
    List<User> findAdmins(String group);
    List<User> findUsers(String group);
    String getRole(User user, BaseMembership entity);
}
