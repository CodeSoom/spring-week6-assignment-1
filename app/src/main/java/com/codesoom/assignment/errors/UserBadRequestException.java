package com.codesoom.assignment.errors;

public class UserBadRequestException extends RuntimeException {
    public UserBadRequestException(String variable) {
        super(String.format("%s 값은 필수입니다", variable));
    }
}
