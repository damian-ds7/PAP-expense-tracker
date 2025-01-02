package com.example.expenseapi.service;

import com.example.expenseapi.pojo.Group;
import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.GroupRepository;
import com.example.expenseapi.repository.RoleRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Group> getAll() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(email);
        return user.map(value ->
                membershipService.getBaseGroupsByUserId(value.getId()).stream()
                        .filter(baseGroup -> baseGroup instanceof Group)
                        .map(baseGroup -> (Group) baseGroup)
                        .collect(Collectors.toList())
        ).orElse(Collections.emptyList());
    }

    @Override
    public Group save(Group entity) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(email);
        Group newGroup = super.save(entity);
        membershipService.save(new Membership(user.get(), newGroup, roleRepository.findById(1L).get()));
        return newGroup;
    }
}
