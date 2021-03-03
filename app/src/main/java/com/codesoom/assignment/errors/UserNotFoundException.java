package com.codesoom.assignment.errors;

/**
 * 유저 탐색 실패 예외.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
