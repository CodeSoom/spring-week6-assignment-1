package com.codesoom.assignment.errors;

public class SessionValidationException extends RuntimeException {
    public SessionValidationException() {
        super("Session validation failed");
    }
}
