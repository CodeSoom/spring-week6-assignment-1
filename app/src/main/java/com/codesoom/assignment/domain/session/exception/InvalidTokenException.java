package com.codesoom.assignment.domain.session.exception;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String token) {
        super("토큰이 유효하지 않습니다: " + token);
    }
}
