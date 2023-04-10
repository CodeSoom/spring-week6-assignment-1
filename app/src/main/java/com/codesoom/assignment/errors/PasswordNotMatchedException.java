package com.codesoom.assignment.errors;

public class PasswordNotMatchedException extends RuntimeException {

    public PasswordNotMatchedException(String email) {
        super(email + " 의 비밀번호가 틀립니다. 다시 한번 시도해주세요.");
    }
}
