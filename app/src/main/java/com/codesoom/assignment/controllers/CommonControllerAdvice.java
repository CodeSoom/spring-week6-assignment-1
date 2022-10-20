package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.exception.InvalidTokenException;
import com.codesoom.assignment.exception.ProductNotFoundException;
import com.codesoom.assignment.exception.UserEmailDuplicationException;
import com.codesoom.assignment.exception.UserNotFoundException;
import com.codesoom.assignment.exception.PasswordMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class CommonControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFound() {
        return new ErrorResponse("Product not found");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound() {
        return new ErrorResponse("User not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicationException.class)
    public ErrorResponse handleUserEmailIsAlreadyExisted() {
        return new ErrorResponse("User's email address is already existed");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordMismatchException.class)
    public ErrorResponse handleWrongPassword(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse handleValidError(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTokenException.class)
    public ErrorResponse handleUnAuthorizedRequest(Exception e) {
        return new ErrorResponse(e.getMessage());
    }
}
