package com.codesoom.assignment.errors;

/**
 * 회원 중 이미 존재하는 email 이 있을 경우 던집니다.
 */
public class UserEmailDuplicationException extends RuntimeException {
    public UserEmailDuplicationException(String email) {
        super("User email is alreay existed: " + email);
    }
}
