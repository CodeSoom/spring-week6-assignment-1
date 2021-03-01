package com.codesoom.assignment.errors;

public class WrongUserPasswordException extends RuntimeException {
    public WrongUserPasswordException() {
        super("Wrong password");
    }
}
