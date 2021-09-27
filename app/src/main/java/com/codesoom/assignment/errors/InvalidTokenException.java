package com.codesoom.assignment.errors;

public class InvalidTokenException extends RuntimeException {

    /**
     * 잘못된 토큰일 경우 던집니다.
     */
    public InvalidTokenException() {
        super("잘못된 토큰입니다.");
    }
}
