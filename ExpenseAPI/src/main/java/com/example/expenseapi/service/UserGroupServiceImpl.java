package com.example.expenseapi.service;

import com.example.expenseapi.pojo.Group;
import com.example.expenseapi.repository.GroupRepository;

public class UserGroupServiceImpl extends GenericServiceImpl<Group, Long> implements UserGroupService{
    public UserGroupServiceImpl(GroupRepository repository) {
        super(repository);
    }
}
