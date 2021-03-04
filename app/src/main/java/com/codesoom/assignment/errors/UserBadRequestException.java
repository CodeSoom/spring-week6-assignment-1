package com.codesoom.assignment.errors;

public class UserBadRequestException extends RuntimeException {
    public UserBadRequestException() {
        super("User bad request");
    }
}
