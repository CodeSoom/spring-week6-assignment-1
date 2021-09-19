package com.codesoom.assignment.errors;

public class UserEmailNotFoundException extends RuntimeException {
    public UserEmailNotFoundException(String email) {
        super("User Email not found: " + email);
    }
}