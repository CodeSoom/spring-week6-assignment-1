package com.codesoom.assignment.errors;

public class EmailNotFoundException extends RuntimeException{
    public EmailNotFoundException(String email) {
        super("이메일을 찾을 수 없습니다." + email);
    }
}
