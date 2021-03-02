package com.codesoom.assignment.auth.application;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String accessToken) {
        super("Invalid access token: " + accessToken);
    }

    public InvalidTokenException(String accessToken, Throwable cause) {
        super("Invalid access token: " + accessToken, cause);
    }
}
