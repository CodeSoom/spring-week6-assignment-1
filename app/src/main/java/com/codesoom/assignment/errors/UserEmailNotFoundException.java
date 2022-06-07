package com.codesoom.assignment.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "이메일이 존재하지 않습니다 다시 확인해주세요")
public class UserEmailNotFoundException extends RuntimeException {

    public UserEmailNotFoundException(String email) {
        super(email + "은 존재하지 이메일입니다");
    }
}
