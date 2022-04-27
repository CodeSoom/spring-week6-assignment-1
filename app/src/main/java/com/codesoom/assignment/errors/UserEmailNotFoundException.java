package com.codesoom.assignment.errors;

public class UserEmailNotFoundException extends RuntimeException {

    public UserEmailNotFoundException(String message) {
        super(message);
    }
}
