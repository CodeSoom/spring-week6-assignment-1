package com.codesoom.assignment.errors;

/**
 * email 을 이용해 회원을 조회했을 때 못찾았다면 던집니다.
 */
public class UserNotFoundByEmailException extends RuntimeException {
    public UserNotFoundByEmailException(String email) {
        super("User not found: " + email);
    }
}

