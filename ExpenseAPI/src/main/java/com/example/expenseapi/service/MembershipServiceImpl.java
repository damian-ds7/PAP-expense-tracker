package com.example.expenseapi.service;

import com.example.expenseapi.dto.UserDTO;
import com.example.expenseapi.exception.ForbiddenRequestException;
import com.example.expenseapi.mapper.UserMapper;
import com.example.expenseapi.pojo.BaseGroup;
import com.example.expenseapi.pojo.Group;
import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.MembershipRepository;
import com.example.expenseapi.utils.AuthHelper;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "baseGroups", keyGenerator = "userBasedKeyGenerator")
    public List<BaseGroup> getBaseGroupsByUserId(Long userId) {
        return membershipRepository.findBaseGroupsByUser_Id(userId);
    }

    @Override
    @Cacheable(value = "membershipsByUserId", key = "#userId")
    public List<Membership> getMembershipsByUserId(Long userId) {
        return membershipRepository.findByUserId(userId);
    }

    @Override
    @Cacheable(value = "admins", key = "#group")
    public List<UserDTO> findAdmins(String group) {
        if (!AuthHelper.isGroupNameValid(group)) {
            throw new ForbiddenRequestException("User is not a member of the group");
        }
        return membershipRepository.findAdmins(group)
                .stream().map(userMapper::userToUserDTO)
                .toList();
    }

    @Override
    @Cacheable(value = "users", key = "#group")
    public List<UserDTO> findUsers(String group) {
        if (!AuthHelper.isGroupNameValid(group)) {
            throw new ForbiddenRequestException("User is not a member of the group");
        }
        return membershipRepository.findUsers(group)
                .stream().map(userMapper::userToUserDTO)
                .toList();
    }

    @Override
    @Cacheable(value = "roles", key = "#user.id + '_' + #group.id")
    public String getRole(User user, Group group) {
        return getMembershipsByUserId(user.getId())
                .stream()
                .filter(membership -> membership.getGroup().getId().equals(group.getId()))
                .findFirst()
                .get()
                .getRole()
                .getName();
    }

    @Override
    @CacheEvict(value = {"baseGroups", "activeGroups", "membershipsByUserId"}, keyGenerator = "userBasedKeyGenerator", allEntries = true)
    public Membership save(Membership entity) {
        return super.save(entity);
    }

    @Override
    @CacheEvict(value = {"baseGroups", "activeGroups", "membershipsByUserId"}, keyGenerator = "userBasedKeyGenerator", allEntries = true)
    public Membership update(Long id, Membership entity) {
        return super.update(id, entity);
    }

    @Override
    @CacheEvict(value = {"baseGroups", "activeGroups", "membershipsByUserId"}, keyGenerator = "userBasedKeyGenerator", allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @CacheEvict(value = {"baseGroups", "activeGroups", "membershipsByUserId"}, keyGenerator = "userBasedKeyGenerator", allEntries = true)
    public void deleteAllData() {
        super.deleteAllData();
    }

    @Override
    @CacheEvict(value = {"baseGroups", "activeGroups", "membershipsByUserId"}, keyGenerator = "userBasedKeyGenerator", allEntries = true)
    @Transactional
    public void deleteAllMembershipsForUserId(Long id) {
        membershipRepository.deleteAllByUserId(id);
    }

    @Override
    public Boolean isAdmin(String groupName) {
        User user = AuthHelper.getUser();
        return findAdmins(groupName).stream()
                .map(UserDTO::getId)
                .anyMatch(memberId -> memberId.equals(user.getId()));
    }
}
