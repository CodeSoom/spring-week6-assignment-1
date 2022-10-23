package com.codesoom.assignment.exception;

/**
 * 회원 정보를 찾지 못한 경우 던집니다.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
