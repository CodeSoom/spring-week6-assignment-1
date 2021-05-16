package com.codesoom.assignment.errors;

public class AuthenticationFailException extends RuntimeException {
    public AuthenticationFailException(String message) {
        super("Failed to authenticate user: " + message);
    }
}
