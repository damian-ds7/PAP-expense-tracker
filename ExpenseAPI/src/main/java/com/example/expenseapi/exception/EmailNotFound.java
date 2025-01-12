package com.example.expenseapi.exception;

public class EmailNotFound extends RuntimeException {
    public EmailNotFound(String email) {
        super("Email not found: " + email);
    }
}
