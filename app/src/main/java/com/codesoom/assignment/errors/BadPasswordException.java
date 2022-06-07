package com.codesoom.assignment.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "잘못된 패스워드 입니다. 다시 한번 확인해주세요.")
public class BadPasswordException extends RuntimeException {
    public BadPasswordException(String message) {
        super(message);
    }
}
