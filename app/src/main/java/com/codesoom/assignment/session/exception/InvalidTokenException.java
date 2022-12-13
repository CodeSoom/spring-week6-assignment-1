package com.codesoom.assignment.session.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("토큰이 유효하지 않습니다");
    }

    public InvalidTokenException(String token) {
        super("토큰이 유효하지 않습니다: " + token);
    }
}
