package com.example.expenseapi.exception;

public class MethodNotFoundException extends RuntimeException {
    public MethodNotFoundException(String method) {
        super("Method not found: " + method);
    }
}
