package com.codesoom.assignment.errors;

public class LoginNotMatchPasswordException extends RuntimeException{
    public LoginNotMatchPasswordException(String email) {
        super("Does not match password: " + email);
    }
}
