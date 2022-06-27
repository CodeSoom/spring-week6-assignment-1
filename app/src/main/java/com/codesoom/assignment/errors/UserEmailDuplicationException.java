package com.codesoom.assignment.errors;

/**
 * email이 이미 존재할 때 던진다.
 */
public class UserEmailDuplicationException extends RuntimeException {
    public UserEmailDuplicationException(String email) {
        super("User email is alreay existed: " + email);
    }
}
