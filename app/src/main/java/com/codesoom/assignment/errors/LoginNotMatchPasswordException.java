package com.codesoom.assignment.errors;

public class LoginFailureException extends RuntimeException{
    public LoginFailureException() {
        super("로그인에 실패했습니다.");
    }
}
