package com.codesoom.assignment.errors;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("It is Invalid Token.");
    }

    public InvalidTokenException(String accessToken) {
        super(accessToken + " is InvalidToken.");
    }
}
