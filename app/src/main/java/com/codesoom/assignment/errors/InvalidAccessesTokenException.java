package com.codesoom.assignment.errors;

public class InvalidAccessesTokenException extends RuntimeException {
    public InvalidAccessesTokenException(String token) {
        super("Invalid Accesses Token: " + token);
    }
}
