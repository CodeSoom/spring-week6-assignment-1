package com.codesoom.assignment.domain.session.exception;

import com.codesoom.assignment.common.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@SessionErrorAdvice
public class SessionExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTokenException.class)
    public ErrorResponse handleInvalidToken() {
        return new ErrorResponse("토큰이 유효하지 않습니다");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenNotExistException.class)
    public ErrorResponse handleTokenNotExist() {
        return new ErrorResponse("토큰이 존재하지 않습니다");
    }
}
