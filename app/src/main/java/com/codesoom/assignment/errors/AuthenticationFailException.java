package com.codesoom.assignment.errors;

public class AuthenticationFailException  extends RuntimeException {

    public AuthenticationFailException(String message) {
        super("회원 인증에 실패하였습니다. " + message);
    }

}
