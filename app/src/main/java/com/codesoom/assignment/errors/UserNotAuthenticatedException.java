package com.codesoom.assignment.errors;

public class UserNotAuthenticatedException extends RuntimeException {

    public UserNotAuthenticatedException(String email) {
        super(String.format("삭제되었거나 인증에 실패하였습니다: %s", email));
    }
}
