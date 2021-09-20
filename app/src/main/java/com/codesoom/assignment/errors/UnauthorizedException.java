package com.codesoom.assignment.errors;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("토큰을 찾을 수 없습니다.");
    }
}
