package com.example.expenseapi.exception;

public class GroupNotFound extends RuntimeException {
    public GroupNotFound(Long id) {
        super("Group with id " + id + " not found");
    }
}
