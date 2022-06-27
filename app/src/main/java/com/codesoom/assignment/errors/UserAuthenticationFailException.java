package com.codesoom.assignment.errors;

public class UserAuthenticationFailException extends RuntimeException {

    public UserAuthenticationFailException() {
        super("회원 인증에 실패하였습니다.");
    }

    public UserAuthenticationFailException(String cause) {
        super("회원 인증에 실패하였습니다. 원인: " + cause);
    }
}
