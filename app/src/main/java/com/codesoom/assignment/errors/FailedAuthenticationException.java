package com.codesoom.assignment.errors;

public class FailedAuthenticationException extends RuntimeException {
    public FailedAuthenticationException() {
        super("Failed Authentication");
    }
}
