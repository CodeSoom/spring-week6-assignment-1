package com.codesoom.assignment.errors;

import com.codesoom.assignment.dto.UserLoginDataGettable;

/**
 * 유저 인증 실패 예외
 */
public class UserAuthenticationFailedException extends RuntimeException {
    public UserAuthenticationFailedException(UserLoginDataGettable loginData) {
        super("User authentication failed: " + loginData.getEmail());
    }
}
