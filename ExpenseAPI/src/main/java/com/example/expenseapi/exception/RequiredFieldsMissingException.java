package com.example.expenseapi.exception;

public class RequiredFieldsMissingException extends RuntimeException {
    public RequiredFieldsMissingException() {
        super("Title, price, category name and group name are required");
    }
}
