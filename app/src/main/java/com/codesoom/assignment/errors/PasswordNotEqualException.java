package com.codesoom.assignment.errors;

public class PasswordNotEqualException extends RuntimeException {

    public PasswordNotEqualException() {
        super("Not Equal Password");
    }
}
