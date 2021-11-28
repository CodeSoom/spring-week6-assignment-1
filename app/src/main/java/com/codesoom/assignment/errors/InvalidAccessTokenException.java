package com.codesoom.assignment.errors;

public class InvalidAccessTokenException extends RuntimeException {
    public InvalidAccessTokenException(String accessToken) {
        super("Access Token is invalid " + accessToken);
    }
}
