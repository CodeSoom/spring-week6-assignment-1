package com.codesoom.assignment.errors;

public class LoginNotMatchPasswordException extends RuntimeException{
    public LoginNotMatchPasswordException(String email) {
        super("User not match password: " + email);
    }
}
