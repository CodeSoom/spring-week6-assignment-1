package com.codesoom.assignment.errors;

public class LoginFailException extends RuntimeException {
    public LoginFailException() {
        super("로그인에 실패했습니다, 다시 시도해주세요");
    }
}
