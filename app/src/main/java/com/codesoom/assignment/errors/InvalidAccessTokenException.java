package com.codesoom.assignment.errors;

public class InvalidAccessTokenException extends RuntimeException{
    public InvalidAccessTokenException(String token) {
        super("Invalid access token: " + token);
    }

}
