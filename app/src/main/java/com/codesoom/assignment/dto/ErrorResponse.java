package com.codesoom.assignment.dto;

/**
 * 에러 정보 응답.
 */
public class ErrorResponse {
    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
