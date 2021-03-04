package com.codesoom.assignment.errors;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {
        super("검증되지 않은 회원입니다.");
    }
}
