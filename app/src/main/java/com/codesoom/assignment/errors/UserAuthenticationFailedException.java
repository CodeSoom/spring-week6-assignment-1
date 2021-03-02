package com.codesoom.assignment.errors;

public class UserAuthenticationFailedException extends RuntimeException {
    public UserAuthenticationFailedException(String email) {
        super("User authentication failed: " + email);
    }
}
