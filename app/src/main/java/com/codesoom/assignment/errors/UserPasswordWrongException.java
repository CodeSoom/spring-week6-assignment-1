package com.codesoom.assignment.errors;

public class UserPasswordWrongException extends RuntimeException{
    public UserPasswordWrongException() {
        super("User's Password is wrong");
    }
}
