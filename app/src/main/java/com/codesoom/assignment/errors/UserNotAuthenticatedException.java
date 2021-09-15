package com.codesoom.assignment.errors;

import com.codesoom.assignment.domain.User;

public class UserNotAuthenticatedException extends RuntimeException {

    public UserNotAuthenticatedException(User user) {
        super(String.format("삭제되었거나 인증에 실패하였습니다: %s", user.getEmail()));
    }
}
