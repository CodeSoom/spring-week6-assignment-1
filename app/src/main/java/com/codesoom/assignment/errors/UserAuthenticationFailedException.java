package com.codesoom.assignment.errors;

/**
 * 유저 로그인 실패 예외.
 */
public class UserAuthenticationFailedException extends RuntimeException {
    public UserAuthenticationFailedException(String email) {
        super("User authentication failed: " + email);
    }
}
