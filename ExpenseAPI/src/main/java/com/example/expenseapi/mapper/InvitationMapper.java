package com.example.expenseapi.mapper;

import com.example.expenseapi.dto.InvitationDTO;
import com.example.expenseapi.pojo.TemporaryMembership;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvitationMapper {
    @Mapping(source = "user.id", target = "receiver.id")
    @Mapping(source = "user.name", target = "receiver.name")
    @Mapping(source = "user.surname", target = "receiver.surname")
    @Mapping(source = "user.email", target = "receiver.email")
    @Mapping(source = "group", target = "group")
    @Mapping(source = "sender.id", target = "sender.id")
    @Mapping(source = "sender.name", target = "sender.name")
    @Mapping(source = "sender.surname", target = "sender.surname")
    @Mapping(source = "sender.email", target = "sender.email")
    @Mapping(source = "id", target = "id")
    InvitationDTO tempMembershipToInvitation(TemporaryMembership tempMembership);
}
