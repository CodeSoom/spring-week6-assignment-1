package com.codesoom.assignment.errors;

public class LoginWrongPasswordException extends RuntimeException   {
    public LoginWrongPasswordException() {
        super("wrong password");
    }
}
