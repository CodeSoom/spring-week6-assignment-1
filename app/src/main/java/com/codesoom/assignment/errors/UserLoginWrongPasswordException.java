package com.codesoom.assignment.errors;

public class UserLoginWrongPasswordException extends RuntimeException {
    public UserLoginWrongPasswordException() {
        super("일치하는 회원정보가 없습니다. 로그인에 실패하였습니다.");
    }
}
