package com.codesoom.assignment.dto;

public class InvalidAccessTokenException extends RuntimeException{
    public InvalidAccessTokenException(String token) {
        super("Invalid access token: " + token);
    }

}
