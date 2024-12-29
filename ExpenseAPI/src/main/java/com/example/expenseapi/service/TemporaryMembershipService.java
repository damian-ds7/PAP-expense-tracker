package com.example.expenseapi.service;

import com.example.expenseapi.pojo.TemporaryMembership;

import java.util.List;

public interface TemporaryMembershipService extends GenericService<TemporaryMembership, Long> {
    List<TemporaryMembership> getByUserId(Long userId);
}
