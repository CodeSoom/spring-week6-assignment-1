package com.codesoom.assignment.errors;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }

    public UserNotFoundException(String email) {
        super(String.format("%s 이메일로 가입된 회원을 찾지 못했습니다.", email));
    }
}
