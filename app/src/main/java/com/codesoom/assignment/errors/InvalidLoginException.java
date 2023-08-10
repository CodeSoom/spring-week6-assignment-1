package com.codesoom.assignment.errors;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String message) {
        super("Invalid login Exception: " + message);
    }

}
