package com.codesoom.assignment.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
    public UserNotFoundException(String email) {
        super("User not found: " + email);
    }
}
