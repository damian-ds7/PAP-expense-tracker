package com.example.expenseapi.service;

import com.example.expenseapi.pojo.Role;
import com.example.expenseapi.repository.RoleRepository;

public class RoleServiceImpl extends GenericServiceImpl<Role, Long> implements RoleService {
    public RoleServiceImpl(RoleRepository repository) {
        super(repository);
    }
}
