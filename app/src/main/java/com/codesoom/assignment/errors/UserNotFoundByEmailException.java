package com.codesoom.assignment.errors;

public class UserNotFoundByEmailException extends RuntimeException {
    public UserNotFoundByEmailException(String email) {
        super("User not found: " + email);
    }
}

