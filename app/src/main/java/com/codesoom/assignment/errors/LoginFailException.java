package com.codesoom.assignment.errors;

public class LoginFailException extends RuntimeException{
    public LoginFailException() {
        super("Login Fail");
    }

}
