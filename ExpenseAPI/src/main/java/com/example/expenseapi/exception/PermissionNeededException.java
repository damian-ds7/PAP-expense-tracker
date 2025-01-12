package com.example.expenseapi.exception;

public class PermissionNeededException extends RuntimeException {
    public PermissionNeededException(String groupName) {
        super("Your role in " + groupName + " is not sufficient to perform this action");
    }
}
