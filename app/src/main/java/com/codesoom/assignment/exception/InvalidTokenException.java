package com.codesoom.assignment.exception;

/**
 * 요청으로 들어온 토큰이 유효하지 않은 경우 던집니다.
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
