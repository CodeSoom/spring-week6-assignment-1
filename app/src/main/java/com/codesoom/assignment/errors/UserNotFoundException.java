package com.codesoom.assignment.errors;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User not found this id: " + id);
    }

    public UserNotFoundException(String email) {
        super("User not found this email: " + email);
    }
}
