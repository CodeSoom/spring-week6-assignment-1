package com.codesoom.assignment.errors;

/**
 * 로그인 시 잘못된 비밀번호로 요청했을 경우 발생한다.
 */
public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Wrong password");
    }
}
