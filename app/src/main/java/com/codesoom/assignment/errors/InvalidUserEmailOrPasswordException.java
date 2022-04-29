package com.codesoom.assignment.errors;

public class InvalidUserEmailOrPasswordException extends RuntimeException {
    public InvalidUserEmailOrPasswordException() {
        super("Invalid email or password");
    }
}
