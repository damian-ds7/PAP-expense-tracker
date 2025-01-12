package com.example.expenseapi.exception;

public class InvalidKeyTypeException extends RuntimeException {
    public InvalidKeyTypeException(String keyType) {
        super("Invalid key type: " + keyType);
    }
}
