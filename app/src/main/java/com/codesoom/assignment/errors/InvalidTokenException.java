package com.codesoom.assignment.errors;

import io.jsonwebtoken.JwtException;

/**
 * 유효하지 않은 토큰일 경우 던집니다.
 */
public class InvalidTokenException extends JwtException {

    public InvalidTokenException(String message) {
        super(message);
    }
}
