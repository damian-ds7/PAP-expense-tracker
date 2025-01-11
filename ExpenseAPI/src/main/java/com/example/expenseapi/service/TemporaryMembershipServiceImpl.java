package com.example.expenseapi.service;

import com.example.expenseapi.dto.InvitationDTO;
import com.example.expenseapi.dto.MembershipCreateDTO;
import com.example.expenseapi.mapper.InvitationMapper;
import com.example.expenseapi.mapper.UserMapper;
import com.example.expenseapi.pojo.TemporaryMembership;
import com.example.expenseapi.repository.GroupRepository;
import com.example.expenseapi.repository.RoleRepository;
import com.example.expenseapi.repository.TemporaryMembershipRepository;
import com.example.expenseapi.utils.AuthHelper;
import jakarta.transaction.Transactional;
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
    private final InvitationMapper invitationMapper;

    public TemporaryMembershipServiceImpl(TemporaryMembershipRepository repository, UserMapper userMapper, RoleRepository roleRepository, GroupRepository groupRepository, InvitationMapper invitationMapper) {
        super(repository);
        this.temporaryMembershipRepository = repository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
        this.invitationMapper = invitationMapper;
    }

    @Override
    @Cacheable(value = "temporaryMembershipsByUserId", key = "#userId")
    public List<InvitationDTO> getByUserId(Long userId) {
        return temporaryMembershipRepository.findByUserId(userId)
                .stream()
                .map(invitationMapper::tempMembershipToInvitation)
                .toList();
    }

    @Override
    @Cacheable(value = "temporaryMembershipsBySenderId", key = "#senderId")
    public List<InvitationDTO> getBySenderId(Long senderId) {
        return temporaryMembershipRepository.findBySenderId(senderId)
                .stream()
                .map(invitationMapper::tempMembershipToInvitation)
                .toList();
    }

    @Override
    @CacheEvict(value = "temporaryMembershipsByUserId", key = "#temporaryMembershipCreateDTO.user.id")
    public TemporaryMembership save(MembershipCreateDTO temporaryMembershipCreateDTO) {
        TemporaryMembership temporaryMembership = new TemporaryMembership();
        temporaryMembership.setUser(userMapper.UserDTOToUser(temporaryMembershipCreateDTO.getUser()));
        temporaryMembership.setGroup(groupRepository.findById(temporaryMembershipCreateDTO.getGroup().getId()).get());
        temporaryMembership.setRole(roleRepository.findById(2L).get());
        temporaryMembership.setSender(AuthHelper.getUser());
        return temporaryMembershipRepository.save(temporaryMembership);
    }

    @Override
    @CacheEvict(value = "temporaryMembershipsByUserId", key = "T(com.example.expenseapi.utils.AuthHelper).getUser().getId()")
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @CacheEvict(value = "temporaryMembershipsByUserId", key = "T(com.example.expenseapi.utils.AuthHelper).getUser().getId()")
    @Transactional
    public void deleteAllTemporaryMembershipsForUser(Long id) {
        temporaryMembershipRepository.deleteAllByUserId(id);
    }
}
