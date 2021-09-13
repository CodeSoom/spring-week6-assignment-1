package com.codesoom.assignment.errors;

public class InvalidTokenException extends RuntimeException {
    public static final String DEFAULT_MESSAGE_WITHOUT_TOKEN = "로그인되지 않았습니다.";
    public static final String DEFAULT_MESSAGE_WITH_TOKEN = "유효하지 않은 토큰입니다. TOKEN:";

    public InvalidTokenException() {
        super(DEFAULT_MESSAGE_WITHOUT_TOKEN);
    }

    public InvalidTokenException(String token) {
        super(DEFAULT_MESSAGE_WITH_TOKEN + token);
    }
}
