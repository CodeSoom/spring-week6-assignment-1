package com.codesoom.assignment.errors;

public class AuthenticationFailException extends RuntimeException{
    public AuthenticationFailException(String email) {
        super(String.format("회원 인증에 실패하였습니다. ", email));
    }


}
