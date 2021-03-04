package com.codesoom.assignment.errors;

public class WrongUserException extends RuntimeException {
    public WrongUserException() {
        super("Wrong user information");
    }
}
