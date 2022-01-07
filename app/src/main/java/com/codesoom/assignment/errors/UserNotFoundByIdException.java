package com.codesoom.assignment.errors;

/**
 * id 로 회원을 찾지 못했을 경우 발생한다.
 */
public class UserNotFoundByIdException extends RuntimeException {
    public UserNotFoundByIdException(Long id) {
        super("User not found: " + id);
    }
}
