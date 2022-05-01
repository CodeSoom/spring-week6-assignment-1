package com.codesoom.assignment.application.auth;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends RuntimeException {

    private String code;

    public InvalidTokenException(String message){
        super(message);
        this.code = String.valueOf(HttpStatus.UNAUTHORIZED.value());
    }

    public String getCode() {
        return code;
    }

}
