package com.codesoom.assignment.errors;

public class PasswordInValidException  extends RuntimeException{
    public PasswordInValidException() {
        super("Password is invalid!");
    }
}
