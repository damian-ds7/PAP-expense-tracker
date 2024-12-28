package com.example.expenseapi.service;

import com.example.expenseapi.pojo.BaseGroup;
import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.MembershipRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipServiceImpl extends GenericServiceImpl<Membership, Long> implements MembershipService {
    MembershipRepository membershipRepository;
    public MembershipServiceImpl(MembershipRepository repository) {
        super(repository);
        this.membershipRepository = repository;
    }
    @Override
    public List<BaseGroup> getBaseGroupsByUserId(Long userId) {
        return membershipRepository.findBaseGroupsByUser_Id(userId);
    }

    @Override
    public List<Membership> getMembershipsByUserId(Long userId) {
        return membershipRepository.findByUserId(userId);
    }

    @Override
    public List<User> findAdmins(String group) {
        return membershipRepository.findAdmins(group);
    }
}
