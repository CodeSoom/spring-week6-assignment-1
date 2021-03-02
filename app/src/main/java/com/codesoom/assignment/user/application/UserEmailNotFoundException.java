package com.codesoom.assignment.user.application;

public class UserEmailNotFoundException extends RuntimeException {
    public UserEmailNotFoundException(String email) {
        super("User email is not existed: " + email);
    }
}
