package com.codesoom.assignment.errors;

public class UserNotFoundException extends RuntimeException {

    public static final String USER_NOT_FOUND = "User not found: ";

    public UserNotFoundException(Long id) {
        super(USER_NOT_FOUND + id);
    }

    public UserNotFoundException(String email) {
        super(USER_NOT_FOUND + email);
    }
}
