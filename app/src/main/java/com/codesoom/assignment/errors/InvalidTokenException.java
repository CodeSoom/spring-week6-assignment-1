package com.codesoom.assignment.errors;

public class InvalidTokenException extends RuntimeException{
    public static final String DEFAULT_MESSAGE = "유효하지 않은 토큰입니다. TOKEN:";

    public InvalidTokenException(String token) {
        super(DEFAULT_MESSAGE + token);
    }
}
