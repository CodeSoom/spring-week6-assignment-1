package com.codesoom.assignment.errors;

public class LoginInconsistencyException extends RuntimeException{

    public LoginInconsistencyException() {

        super("입력하신 이메일과 패스워드가 일치하지 않습니다.");

    }

}
