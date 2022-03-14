package com.codesoom.assignment.errors;

/**
 * id를 이용해 회원을 찾지 못했을 경우 던집니다.
 */
public class UserNotFoundByIdException extends RuntimeException {
    public UserNotFoundByIdException(Long id) {
        super("User not found: " + id);
    }
}
