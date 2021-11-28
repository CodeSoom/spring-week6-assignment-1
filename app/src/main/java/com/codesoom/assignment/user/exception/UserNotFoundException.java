package com.codesoom.assignment.user.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super(String.format("ID: %d 유저를 찾는데 실패하였습니다", id));
    }
}
