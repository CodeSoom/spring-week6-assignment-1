package com.codesoom.assignment.errors;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String accessToken) {
        super("Invalid token: " + accessToken);
    }
}
