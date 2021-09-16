package com.codesoom.assignment.controllers;

import com.codesoom.assignment.errors.LoginInconsistencyException;
import com.codesoom.assignment.errors.UserEmailDuplicateException;
import com.codesoom.assignment.errors.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserErrorAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicateException.class)
    public UserEmailDuplicateException duplicateException() {
        return new UserEmailDuplicateException();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public UnauthorizedException unauthorizedException() {
        return new UnauthorizedException();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LoginInconsistencyException.class)
    public LoginInconsistencyException loginInconsistencyException() {
        return new LoginInconsistencyException();
    }

}
