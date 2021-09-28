package com.codesoom.assignment.errors;

public class PasswordInvalidException extends RuntimeException{
    public PasswordInvalidException() {
        super("Invalid Password");
    }
}
