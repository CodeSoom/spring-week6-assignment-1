package com.codesoom.assignment.errors;

public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException() {
        super("Wrong password");
    }
}
