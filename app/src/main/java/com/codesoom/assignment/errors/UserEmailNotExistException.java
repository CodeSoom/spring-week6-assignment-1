package com.codesoom.assignment.errors;

public class UserEmailNotExistException extends RuntimeException {

    public UserEmailNotExistException(String message) {
        super(message);
    }
}
