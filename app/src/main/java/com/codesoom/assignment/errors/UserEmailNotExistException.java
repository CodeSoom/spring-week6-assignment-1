package com.codesoom.assignment.errors;

/**
 * 유저 email 탐색 실패 예외.
 */
public class UserEmailNotExistException extends RuntimeException {
    public UserEmailNotExistException(String email) {
        super("User email not exist: " + email);
    }
}
