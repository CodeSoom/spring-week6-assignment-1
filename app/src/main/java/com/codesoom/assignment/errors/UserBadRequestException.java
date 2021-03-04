package com.codesoom.assignment.errors;

public class UserBadRequestException extends RuntimeException {
    public UserBadRequestException(String variable) {
        super("User bad request: " + variable);
    }
}
