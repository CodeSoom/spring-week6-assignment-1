package com.codesoom.assignment.errors;

/**
 * <p>예외를 던지는 케이스
 * <p>1. 유효한 토큰이 아닐 때
 * <p>2. Authroization 헤더가 없거나 값이 비어있을 때
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
