package com.example.expenseapi.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String token) {
        super("Token " + token + " does not exist");
    }
}
