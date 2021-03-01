package com.codesoom.assignment.errors;

public class UserAuthenticationFailException extends RuntimeException {

    public UserAuthenticationFailException(String message) {
        super("회원 인증에 실패하였습니다. 원인 = " + message);
    }

}
