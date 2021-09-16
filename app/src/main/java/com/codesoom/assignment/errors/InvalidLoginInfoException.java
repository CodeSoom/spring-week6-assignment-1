package com.codesoom.assignment.errors;

public class InvalidLoginInfoException extends RuntimeException {
    public InvalidLoginInfoException() {
        super("Invalid login info.");
    }
}
