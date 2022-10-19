package com.codesoom.assignment.exception;

/**
 * 로그인 하지 않은 상태로 권한이 필요한 작업을 요청할 경우 던집니다.
 */
public class UnAuthorizedAccessException extends RuntimeException {
    public UnAuthorizedAccessException(String message) {
        super(message);
    }
}
