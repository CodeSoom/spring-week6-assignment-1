package com.codesoom.assignment.errors;

/**
 * 패스워드가 틀린 경우에 던집니다.
 */
public class PasswordNotCorrectException extends RuntimeException {
    public PasswordNotCorrectException() {
        super("Password not correct");
    }
}
