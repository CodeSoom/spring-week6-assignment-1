package com.codesoom.assignment.errors;

/**
 * 잘못된 토큰 예외
 */
public class InvalidAccessTokenException extends RuntimeException {
    public InvalidAccessTokenException(String accessToken) {
        super("Invalid token: " + accessToken);
    }
}
