package com.codesoom.assignment.errors;

/**
 * 비밀번호가 일치하지 않을 때 던집니다.
 */
public class UserPasswordDoesNotMatchException extends RuntimeException {

    public UserPasswordDoesNotMatchException(String message) {
        super(message);
    }
}
