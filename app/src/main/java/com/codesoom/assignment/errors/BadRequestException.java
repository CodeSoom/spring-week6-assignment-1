package com.codesoom.assignment.errors;

/**
 * 올바르지 않은 형식의 요청이 들어왔을 때 던진다.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("Bad Request");
    }
}
