package com.codesoom.assignment.exception;

/**
 * 저장된 회원 비밀번호와 값이 다를 때 던집니다.
 */
public class WrongUserPasswordException extends RuntimeException {
    public WrongUserPasswordException(String message) {
        super(message);
    }
}
