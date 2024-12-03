package com.example.expenseapi.service;

import com.example.expenseapi.pojo.UserGroup;
import com.example.expenseapi.repository.UserGroupRepository;

public class UserGroupServiceImpl extends GenericServiceImpl<UserGroup, Long> implements UserGroupService{
    public UserGroupServiceImpl(UserGroupRepository repository) {
        super(repository);
    }
}
