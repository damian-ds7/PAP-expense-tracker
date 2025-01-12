package com.example.expenseapi.exception;

public class UserNotInGroupException extends RuntimeException {
    public UserNotInGroupException(String group, Long id) {
        super("User with id " + id + " is not in the group " + group);
    }
}
