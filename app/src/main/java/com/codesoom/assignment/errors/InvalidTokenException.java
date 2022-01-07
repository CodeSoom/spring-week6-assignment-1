package com.codesoom.assignment.errors;

/**
 * 토큰이 유효하지 않거나, null, white space, 공백일 경우 던집니다.
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super("Invalid access token : " + token);
    }
}
