package com.example.expenseapi.service;

import com.example.expenseapi.dto.MembershipCreateDTO;
import com.example.expenseapi.mapper.UserMapper;
import com.example.expenseapi.pojo.TemporaryMembership;
import com.example.expenseapi.repository.GroupRepository;
import com.example.expenseapi.repository.RoleRepository;
import com.example.expenseapi.repository.TemporaryMembershipRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemporaryMembershipServiceImpl extends GenericServiceImpl<TemporaryMembership, Long> implements TemporaryMembershipService {
    private final TemporaryMembershipRepository temporaryMembershipRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;

    public TemporaryMembershipServiceImpl(TemporaryMembershipRepository repository, UserMapper userMapper, RoleRepository roleRepository, GroupRepository groupRepository) {
        super(repository);
        this.temporaryMembershipRepository = repository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    @Cacheable(value = "temporaryMembershipsByUserId", key = "#userId")
    public List<TemporaryMembership> getByUserId(Long userId) {
        return temporaryMembershipRepository.findByUserId(userId);
    }

    @Override
    @CacheEvict(value = "temporaryMembershipsByUserId", key = "#temporaryMembershipCreateDTO.user.id")
    public TemporaryMembership save(MembershipCreateDTO temporaryMembershipCreateDTO) {
        TemporaryMembership temporaryMembership = new TemporaryMembership();
        temporaryMembership.setUser(userMapper.UserDTOToUser(temporaryMembershipCreateDTO.getUser()));
        temporaryMembership.setGroup(groupRepository.findById(temporaryMembershipCreateDTO.getGroup().getId()).get());
        temporaryMembership.setRole(roleRepository.findById(2L).get());
        return temporaryMembershipRepository.save(temporaryMembership);
    }

    @Override
    @CacheEvict(value = "temporaryMembershipsByUserId", key = "T(com.example.expenseapi.utils.AuthHelper).getUser().getId()")
    public void delete(Long id) {
        super.delete(id);
    }
}
