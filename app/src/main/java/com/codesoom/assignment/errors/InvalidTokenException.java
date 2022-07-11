package com.codesoom.assignment.errors;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String jwt) {
        super("Token Failed at verification : " + jwt);
    }
}
