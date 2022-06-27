package com.codesoom.assignment.errors;

/**
 * User를 찾지 못했을 때 던진다.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
