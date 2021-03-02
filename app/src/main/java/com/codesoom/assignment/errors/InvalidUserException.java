package com.codesoom.assignment.errors;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {
        super("없는 이메일 이거나 패스워드가 일치하지 않습니다.");
    }
}
