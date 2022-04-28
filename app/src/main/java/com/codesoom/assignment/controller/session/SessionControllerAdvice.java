package com.codesoom.assignment.controller.session;

import com.codesoom.assignment.application.auth.InvalidPasswordException;
import com.codesoom.assignment.application.users.UserNotFoundException;
import com.codesoom.assignment.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.codesoom.assignment.controller.session")
public class SessionControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return ErrorResponse.of(e.getCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidPasswordException.class)
    public ErrorResponse handleInvalidPasswordException(InvalidPasswordException e) {
        return ErrorResponse.of(e.getCode(), e.getMessage());
    }

}
