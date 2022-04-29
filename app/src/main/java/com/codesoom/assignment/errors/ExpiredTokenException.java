package com.codesoom.assignment.errors;

import io.jsonwebtoken.JwtException;

/**
 * 만료된 토큰일 경우 던집니다.
 */
public final class ExpiredTokenException extends JwtException {

    public ExpiredTokenException(final String message) {
        super(message);
    }
}
