package com.example.expenseapi.service;

import com.example.expenseapi.pojo.BaseGroup;
import com.example.expenseapi.pojo.BaseMembership;
import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.User;

import java.util.List;

public interface MembershipService extends GenericService<Membership, Long> {
    List<BaseGroup> getBaseGroupsByUserId(Long userId);
    List<Membership> getMembershipsByUserId(Long userId);
    List<User> findAdmins(String group);
    List<User> findUsers(String group);
    String getRole(User user, BaseMembership entity);
}
