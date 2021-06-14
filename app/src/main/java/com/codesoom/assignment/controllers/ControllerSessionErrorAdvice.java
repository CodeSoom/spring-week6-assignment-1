package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.UserPasswordMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 세션에 관한 예외 처리 핸들러
 */
@ResponseBody
@ControllerAdvice
public class ControllerSessionErrorAdvice {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTokenException.class)
    public ErrorResponse handleInvalidAccessTokenException() {
        return new ErrorResponse("Token is invalid");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserPasswordMismatchException.class)
    public ErrorResponse handleUserLoginFailedException() {
        return new ErrorResponse("Login failed, checking your password");
    }
}
