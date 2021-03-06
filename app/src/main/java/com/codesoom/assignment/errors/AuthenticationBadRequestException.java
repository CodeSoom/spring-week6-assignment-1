package com.codesoom.assignment.errors;

public class AuthenticationBadRequestException extends RuntimeException{
    public AuthenticationBadRequestException() {
        super("Authentication bad request");
    }
}
