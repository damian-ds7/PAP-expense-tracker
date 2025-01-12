package com.example.expenseapi.exception;

public class UserAlreadyInGroupException extends RuntimeException{
    public UserAlreadyInGroupException(Long userId, String groupName) {
        super("User with id: " + userId + " is already in group: " + groupName);
    }
}
