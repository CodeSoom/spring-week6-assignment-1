package com.codesoom.assignment.errors;

public class InvalidUserDataException extends RuntimeException {
    public InvalidUserDataException(String email) {
        super("User data is invalid: " + email);
    }
}
