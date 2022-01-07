package com.codesoom.assignment.errors;

/**
 * email을 이용해 회원을 조회했을 때 찾지 못했을 경우 발생한다.
 */
public class UserNotFoundByEmailException extends RuntimeException {
    public UserNotFoundByEmailException(String email) {
        super("User not found: " + email);
    }
}

