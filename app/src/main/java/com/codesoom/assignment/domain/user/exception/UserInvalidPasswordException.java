package com.codesoom.assignment.domain.user.exception;

public class UserInvalidPasswordException extends RuntimeException{
    public UserInvalidPasswordException(String email) {
        super("비밀번호가 일치하지 않습니다: " + email);
    }
}
