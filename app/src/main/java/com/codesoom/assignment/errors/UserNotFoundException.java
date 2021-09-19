package com.codesoom.assignment.errors;

public class UserNotFoundException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "User not found: ";

    public UserNotFoundException(Long id) {
        super(DEFAULT_MESSAGE + id);
    }

    public UserNotFoundException(String email) {
        super(DEFAULT_MESSAGE + email);
    }
}
