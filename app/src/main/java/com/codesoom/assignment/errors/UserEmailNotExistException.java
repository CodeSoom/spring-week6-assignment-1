package com.codesoom.assignment.errors;

public class UserEmailNotExistException extends RuntimeException {
    public UserEmailNotExistException(String email) {
        super("User email not exist: " + email);
    }
}
