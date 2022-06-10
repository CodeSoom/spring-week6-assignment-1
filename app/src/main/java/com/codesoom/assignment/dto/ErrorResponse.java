package com.codesoom.assignment.dto;

/**
 * Error 메시지를 반환할 때 사용하는 DTO 클래스
 */
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
