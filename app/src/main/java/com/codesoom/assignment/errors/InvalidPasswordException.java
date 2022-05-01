package com.codesoom.assignment.errors;

/**
 * 유효하지 않은 비밀번호일 경우 던집니다.
 */
public class InvalidPasswordException extends IllegalArgumentException {
    
    public InvalidPasswordException(String message) {
        super(message);
    }
}
