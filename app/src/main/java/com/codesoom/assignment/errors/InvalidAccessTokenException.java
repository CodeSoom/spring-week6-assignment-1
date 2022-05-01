package com.codesoom.assignment.errors;

public class InvalidAccessTokenException extends RuntimeException {
    public InvalidAccessTokenException() {
        super("검증되지 않은 토큰입니다");
    }
}
