package com.example.expenseapi.exception;

public class MembershipNotFoundException extends RuntimeException {
    public MembershipNotFoundException(Long userId, Long groupId) {
        super("Membership not found for userId= " + userId + "and groupId= " + groupId);
    }
}
