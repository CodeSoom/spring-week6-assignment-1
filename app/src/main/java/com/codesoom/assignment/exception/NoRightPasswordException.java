package com.codesoom.assignment.exception;

public class NoRightPasswordException extends RuntimeException {
    public NoRightPasswordException(String password) {
        super("No right password - " + password);
    }
}
