package com.codesoom.assignment.errors;

public class InvalidAccessTokenException extends RuntimeException {
    public InvalidAccessTokenException(String accessToken) {
        super("Invalid AccessToken: " + accessToken);
    }
}
