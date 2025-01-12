package com.example.expenseapi.exception;

public class InvitationAlreadySentException extends RuntimeException {
    public InvitationAlreadySentException(Long userId, String groupName) {
        super("Invitation already sent to user with id: " + userId + " for group: " + groupName);
    }
}
