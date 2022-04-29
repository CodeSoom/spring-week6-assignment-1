package com.codesoom.assignment.errors;

/**
 * 일치하지 않는 비밀번호일 때 예외를 던진다.
 */
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super();
    }
}
