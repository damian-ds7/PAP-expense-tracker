package com.example.expenseapi.service;

import com.example.expenseapi.dto.UserDTO;
import com.example.expenseapi.mapper.UserMapper;
import com.example.expenseapi.pojo.BaseGroup;
import com.example.expenseapi.pojo.BaseMembership;
import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.MembershipRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipServiceImpl extends GenericServiceImpl<Membership, Long> implements MembershipService {
    MembershipRepository membershipRepository;
    UserMapper userMapper;
    public MembershipServiceImpl(MembershipRepository repository, UserMapper userMapper) {
        super(repository);
        this.membershipRepository = repository;
        this.userMapper = userMapper;
    }
    @Override
    public List<BaseGroup> getBaseGroupsByUserId(Long userId) {
        return membershipRepository.findBaseGroupsByUser_Id(userId);
    }

    @Override
    public List<Group> getGroupsByUserId(Long userId) {
        return membershipRepository.findGroupsByUserId(userId);
    }

    @Override
    public List<Membership> getMembershipsByUserId(Long userId) {
        return membershipRepository.findByUserId(userId);
    }

    @Override
    public List<UserDTO> findAdmins(String group) {
        return membershipRepository.findAdmins(group)
                .stream().map(userMapper::userToUserDTO)
                .toList();
    }

    @Override
    public List<UserDTO> findUsers(String group) {
        return membershipRepository.findUsers(group)
                .stream().map(userMapper::userToUserDTO)
                .toList();
    }

    @Override
    public String getRole(User user, BaseMembership entity) {
        return getMembershipsByUserId(user.getId())
                .stream()
                .filter(membership -> membership.getGroup().getId().equals(entity.getGroup().getId()))
                .findFirst()
                .get()
                .getRole()
                .getName();
    }

}
