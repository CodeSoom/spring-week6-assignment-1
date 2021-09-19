package com.codesoom.assignment.errors;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {
        super("Invalid email.");
    }
}
