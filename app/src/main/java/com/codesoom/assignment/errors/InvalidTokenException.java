package com.codesoom.assignment.errors;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super("토큰이 유효하지 않습니다. 문제의 토큰: " + token);
    }
}
