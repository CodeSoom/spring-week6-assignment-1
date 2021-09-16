package com.codesoom.assignment.errors;

public class NotValidTokenException extends RuntimeException{

    public NotValidTokenException() {
        super("유효하지 않은 토큰입니다.");
    }

}
