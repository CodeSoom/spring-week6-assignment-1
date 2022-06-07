package com.codesoom.assignment.errors;

public class UserEmailNotFoundException extends RuntimeException {

    public UserEmailNotFoundException(String email) {
        super(email + "은 존재하지 이메일입니다");
    }
}
