package com.codesoom.assignment.errors;

public class LoginFailException extends RuntimeException {
    public LoginFailException(String email) {
        super("Login fail - email: " + email);
    }
}
