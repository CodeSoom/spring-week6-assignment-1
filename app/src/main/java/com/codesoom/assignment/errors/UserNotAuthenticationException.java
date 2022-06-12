package com.codesoom.assignment.errors;

public class UserNotAuthenticationException extends RuntimeException {
    public UserNotAuthenticationException(String email) {
        super("User is not authenticated. email: " + email);
    }
}
