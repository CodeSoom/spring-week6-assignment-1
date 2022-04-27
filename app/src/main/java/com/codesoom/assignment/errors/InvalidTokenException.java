package com.codesoom.assignment.errors;

/**
 * 유효하지 않은 토큰일 경우 던집니다.
 */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }
}
