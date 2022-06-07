package com.codesoom.assignment.errors;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
