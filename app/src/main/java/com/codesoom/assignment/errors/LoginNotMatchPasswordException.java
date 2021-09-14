package com.codesoom.assignment.errors;

public class LoginNotMatchPasswordException extends RuntimeException{
    public LoginNotMatchPasswordException() {
        super("Does not match password");
    }
}
