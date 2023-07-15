package com.codesoom.assignment.errors;

public class InvalidAccessTokenException extends RuntimeException{
    public InvalidAccessTokenException() {
        super("Invalid access token" );
    }

}
