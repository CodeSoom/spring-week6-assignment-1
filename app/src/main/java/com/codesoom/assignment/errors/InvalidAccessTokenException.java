package com.codesoom.assignment.errors;

public class InvalidAccessTokenException extends RuntimeException {

    public InvalidAccessTokenException(String message) {
        super("Invalid access token : " + message);
    }

    public InvalidAccessTokenException() {
        super("Invalid access token");
    }


}
