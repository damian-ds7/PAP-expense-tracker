package com.example.expenseapi.service;

import com.example.expenseapi.dto.InvitationDTO;
import com.example.expenseapi.dto.MembershipCreateDTO;
import com.example.expenseapi.exception.*;
import com.example.expenseapi.mapper.InvitationMapper;
import com.example.expenseapi.mapper.UserMapper;
import com.example.expenseapi.pojo.TemporaryMembership;
import com.example.expenseapi.repository.GroupRepository;
import com.example.expenseapi.repository.RoleRepository;
import com.example.expenseapi.repository.TemporaryMembershipRepository;
import com.example.expenseapi.repository.UserRepository;
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
    private final MembershipService membershipService;
    private final UserRepository userRepository;

    public TemporaryMembershipServiceImpl(TemporaryMembershipRepository repository, UserMapper userMapper, RoleRepository roleRepository, GroupRepository groupRepository, InvitationMapper invitationMapper, MembershipService membershipService, UserRepository userRepository) {
        super(repository);
        this.temporaryMembershipRepository = repository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
        this.invitationMapper = invitationMapper;
        this.membershipService = membershipService;
        this.userRepository = userRepository;
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
    @CacheEvict(value = {"temporaryMembershipsByUserId", "temporaryMembershipsBySenderId"}, allEntries = true)
    public TemporaryMembership save(MembershipCreateDTO temporaryMembershipCreateDTO) {
        if (!membershipService.getRole(AuthHelper.getUser(), temporaryMembershipCreateDTO.getGroup()).equals("admin")) {
            throw new PermissionNeededException(temporaryMembershipCreateDTO.getGroup().getName());
        }
        if (userRepository.findByEmail(temporaryMembershipCreateDTO.getUser().getEmail()).isEmpty()) {
            throw new UserNotFoundException(temporaryMembershipCreateDTO.getUser().getEmail());
        }
        if (AuthHelper.getAllGroups().stream().noneMatch(group -> group.getName().equals(temporaryMembershipCreateDTO.getGroup().getName()))) {
            throw new GroupNotFound(temporaryMembershipCreateDTO.getGroup().getId());
        }
        if (membershipService.getMembershipsByUserId(temporaryMembershipCreateDTO.getUser().getId())
                .stream()
                .anyMatch(membership -> membership.getGroup().getId().equals(temporaryMembershipCreateDTO.getGroup().getId()))) {
            throw new UserAlreadyInGroupException(temporaryMembershipCreateDTO.getUser().getId(), temporaryMembershipCreateDTO.getGroup().getName());
        }
        if (temporaryMembershipRepository.findByUserId(temporaryMembershipCreateDTO.getUser().getId())
                .stream()
                .anyMatch(temporaryMembership -> temporaryMembership.getGroup().getId().equals(temporaryMembershipCreateDTO.getGroup().getId()))) {
            throw new InvitationAlreadySentException(temporaryMembershipCreateDTO.getUser().getId(), temporaryMembershipCreateDTO.getGroup().getName());
        }
        TemporaryMembership temporaryMembership = new TemporaryMembership();
        temporaryMembership.setUser(userMapper.UserDTOToUser(temporaryMembershipCreateDTO.getUser()));
        temporaryMembership.setGroup(groupRepository.findById(temporaryMembershipCreateDTO.getGroup().getId())
                .orElseThrow(() -> new GroupNotFound(temporaryMembershipCreateDTO.getGroup().getId())));
        temporaryMembership.setRole(roleRepository.findById(2L)
                .orElseThrow(() -> new RoleNotFoundException(2L)));
        temporaryMembership.setSender(AuthHelper.getUser());
        return temporaryMembershipRepository.save(temporaryMembership);
    }

    @Override
    @CacheEvict(value = {"temporaryMembershipsByUserId", "temporaryMembershipsBySenderId", "users", "admins"}, allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
        @CacheEvict(value = {"temporaryMembershipsByUserId", "temporaryMembershipsBySenderId"}, allEntries = true)
    @Transactional
    public void deleteAllTemporaryMembershipsForUser(Long id) {
        temporaryMembershipRepository.deleteAllByUserId(id);
    }
}
