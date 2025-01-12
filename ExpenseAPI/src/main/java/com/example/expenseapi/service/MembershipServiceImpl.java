package com.example.expenseapi.service;

import com.example.expenseapi.dto.UserDTO;
import com.example.expenseapi.exception.*;
import com.example.expenseapi.mapper.UserMapper;
import com.example.expenseapi.pojo.BaseGroup;
import com.example.expenseapi.pojo.Group;
import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.MembershipRepository;
import com.example.expenseapi.repository.RoleRepository;
import com.example.expenseapi.utils.AuthHelper;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class MembershipServiceImpl extends GenericServiceImpl<Membership, Long> implements MembershipService {
    private final MembershipRepository membershipRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final ExpenseService expenseService;

    public MembershipServiceImpl(MembershipRepository repository, UserMapper userMapper, RoleRepository roleRepository, ExpenseService expenseService) {
        super(repository);
        this.membershipRepository = repository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.expenseService = expenseService;
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
        return findUsersForGroup(group, membershipRepository::findAdmins);
    }

    @Override
    @Cacheable(value = "users", key = "#group")
    public List<UserDTO> findUsers(String group) {
        return findUsersForGroup(group, membershipRepository::findUsers);
    }

    @Override
    @Cacheable(value = "roles", key = "#user.id + '_' + #group.id")
    public String getRole(User user, Group group) {
        return getMembershipsByUserId(user.getId())
                .stream()
                .filter(membership -> membership.getGroup().getId().equals(group.getId()))
                .findFirst()
                .orElseThrow(()->new MembershipNotFoundException(user.getId(), group.getId()))
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
    @Transactional
    public void deleteMembership(Long userId, String groupName) {
        if(!isAdmin(groupName) && !userId.equals(AuthHelper.getUser().getId())) {
            throw new BadRequestException("User does not have admin privileges");
        }
        expenseService.deleteAllExpensesForUserIdAndGroupName(userId, groupName);
        membershipRepository.deleteByUserIdAndGroupName(userId, groupName);
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
    public Boolean isAdmin(String group) {
        User user = AuthHelper.getUser();
        return findUsersForGroup(group, membershipRepository::findAdmins).stream()
                .map(UserDTO::getId)
                .anyMatch(memberId -> memberId.equals(user.getId()));
    }

    @Override
    @CacheEvict(value = {"baseGroups", "activeGroups", "membershipsByUserId"}, keyGenerator = "userBasedKeyGenerator", allEntries = true)
    public void changeRole(String groupName, String role, Long userId) {
        if (!isAdmin(groupName)) {
            throw new PermissionNeededException(groupName);
        }
        Membership membership = membershipRepository.findByUserIdAndGroupName(userId, groupName)
                .orElseThrow(() -> new UserNotInGroupException(groupName, userId));
        membership.setRole(roleRepository.findByName(role)
                .orElseThrow(() -> new RoleNotFoundException(role)));
        membershipRepository.save(membership);
    }

    private List<UserDTO> findUsersForGroup(String group, Function<String, List<User>> repoMethod) {
        if (AuthHelper.isGroupNameInvalid(group)) {
            throw new UserNotInGroupException(group, AuthHelper.getUser().getId());
        }
        return repoMethod.apply(group)
                .stream()
                .map(userMapper::userToUserDTO)
                .toList();
    }

    @Override
    public String getCurrentRole(String groupName, Long userId) {
        return membershipRepository.findByUserIdAndGroupName(userId, groupName)
                .map(membership -> membership.getRole().getName())
                .orElseThrow(() -> new BadRequestException("No role for this user in this group"));
    }
}
