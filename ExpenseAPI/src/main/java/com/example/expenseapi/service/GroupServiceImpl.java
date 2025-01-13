package com.example.expenseapi.service;

import com.example.expenseapi.exception.RoleNotFoundException;
import com.example.expenseapi.exception.UserNotFoundException;
import com.example.expenseapi.pojo.*;
import com.example.expenseapi.repository.GroupRepository;
import com.example.expenseapi.repository.RoleRepository;
import com.example.expenseapi.utils.AuthHelper;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GroupServiceImpl extends GenericServiceImpl<Group, Long> implements GroupService {
    private final UserService userService;
    private final MembershipService membershipService;
    private final RoleRepository roleRepository;

    public GroupServiceImpl(GroupRepository repository, UserService userService, MembershipService membershipService, RoleRepository roleRepository) {
        super(repository);
        this.userService = userService;
        this.membershipService = membershipService;
        this.roleRepository = roleRepository;
    }

    @Override
    @Cacheable(value = "baseGroups", keyGenerator = "userBasedKeyGenerator")
    public List<BaseGroup> getBaseGroups() {
        String email = AuthHelper.getUserEmail();
        User userEntity = userService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return membershipService.getBaseGroupsByUserId(userEntity.getId());
    }

    @Override
    @Cacheable(value = "activeGroups", keyGenerator = "userBasedKeyGenerator")
    public List<Group> getActiveGroups() {
        return getBaseGroups()
                .stream()
                .filter(baseGroup -> baseGroup instanceof Group)
                .map(baseGroup -> (Group) baseGroup)
                .toList();

    }

    @Override
    @CacheEvict(value = {"baseGroups", "activeGroups", "admins", "users"}, keyGenerator = "userBasedKeyGenerator", allEntries = true)
    public Group save(Group entity) {
        String email = AuthHelper.getUserEmail();
        User userEntity = userService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        Role role = roleRepository.findById(1L)
                .orElseThrow(() -> new RoleNotFoundException(1L));
        Group newGroup = super.save(entity);
        membershipService.save(new Membership(userEntity, newGroup, role));
        return newGroup;

    }

    @Override
    @CacheEvict(value = {"baseGroups", "activeGroups"}, keyGenerator = "userBasedKeyGenerator", allEntries = true)
    @Transactional
    public void delete(Long id) {
        membershipService.deleteAllMembershipsForGroupId(id);
        super.delete(id);
    }

    @Override
    @CacheEvict(value = {"baseGroups", "activeGroups"}, keyGenerator = "userBasedKeyGenerator", allEntries = true)
    public void deleteAllData() {
        super.deleteAllData();
    }

    @Override
    @CacheEvict(value = {"baseGroups", "activeGroups"}, keyGenerator = "userBasedKeyGenerator", allEntries = true)
    public Group update(Long id, Group entity) {
        return super.update(id, entity);
    }
}
