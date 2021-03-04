package com.codesoom.assignment.errors;

public class UserEmailNotExistedException extends RuntimeException {
    public UserEmailNotExistedException() {
        super("User email not existed");
    }
}
