package com.example.expenseapi.service;

import com.example.expenseapi.dto.InvitationDTO;
import com.example.expenseapi.dto.MembershipCreateDTO;
import com.example.expenseapi.pojo.TemporaryMembership;

import java.util.List;

public interface TemporaryMembershipService extends GenericService<TemporaryMembership, Long> {
    List<InvitationDTO> getByUserId(Long userId);
    TemporaryMembership save(MembershipCreateDTO temporaryMembershipCreateDTO);

    List<InvitationDTO> getBySenderId(Long senderId);
}
