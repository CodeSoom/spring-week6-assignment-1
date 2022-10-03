package com.codesoom.assignment.errors;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Object id) {
        super("User not found: " + id);
    }
}
