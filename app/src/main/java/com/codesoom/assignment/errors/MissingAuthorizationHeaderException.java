package com.codesoom.assignment.errors;

public class MissingAuthorizationHeaderException extends RuntimeException {

    public MissingAuthorizationHeaderException() {
        super("Authorization is missed");
    }
}
