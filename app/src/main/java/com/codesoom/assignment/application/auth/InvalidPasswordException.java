package com.codesoom.assignment.application.auth;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends RuntimeException {

    private String code;

    public InvalidPasswordException(String message, String code) {
        super(message);
        this.code = code;
    }

    public InvalidPasswordException(String message) {
        this(message, String.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

    public String getCode() {
        return code;
    }
}
