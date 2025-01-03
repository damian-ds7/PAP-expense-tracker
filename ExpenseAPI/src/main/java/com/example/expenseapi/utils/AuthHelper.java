package com.example.expenseapi.utils;

import com.example.expenseapi.pojo.BaseGroup;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.MembershipRepository;
import com.example.expenseapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AuthHelper {
    private static UserRepository userRepository;
    private static MembershipRepository membershipRepository;

    @Autowired
    public AuthHelper(UserRepository userRepository, MembershipRepository membershipRepository) {
        AuthHelper.userRepository = userRepository;
        AuthHelper.membershipRepository = membershipRepository;
    }

    public static String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static String getGroupName() {
        return getAllGroups().isEmpty() ? "" : getAllGroups().getFirst().getName();
    }

    public static List<BaseGroup> getAllGroups() {
        Optional<User> user = userRepository.findByEmail(getUserEmail());
        List<BaseGroup> groups = List.of();
        if (user.isPresent()) {
            groups = membershipRepository.findBaseGroupsByUser_Id(user.get().getId());
        }
        return groups;
    }

    public static boolean isGroupNameValid(String name) {
        return getAllGroups().stream()
                .anyMatch(group -> group.getName().equals(name));
    }
}
