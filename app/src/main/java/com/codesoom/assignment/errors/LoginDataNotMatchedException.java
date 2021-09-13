package com.codesoom.assignment.errors;

public class LoginDataNotMatchedException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "아이디 혹은 비밀번호가 일치하지 않습니다.";

    public LoginDataNotMatchedException() {
        super(DEFAULT_MESSAGE);
    }
}
