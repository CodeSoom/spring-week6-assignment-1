package com.codesoom.assignment.errors;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String email) {
        super("Login failed: " + email);
    }
}
