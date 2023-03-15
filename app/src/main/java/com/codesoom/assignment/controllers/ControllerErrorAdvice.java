package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.errors.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ControllerAdvice
public class ControllerErrorAdvice {
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFound() {
        return new ErrorResponse("404", "Product not found");
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ErrorResponse missingRequestHeaderException() {
        return new ErrorResponse("401", "MissingRequestHeaderException");
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(MyException.class)
    public ResponseEntity<ErrorResponse> myException(MyException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(ErrorResponse.builder()
                        .code(String.valueOf(e.getStatusCode()))
                        .message(e.getMessage())
                        .build());
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound() {
        return new ErrorResponse("404", "User not found");
    }

    @ExceptionHandler(UserEmailDuplicationException.class)
    public ErrorResponse handleUserEmailIsAlreadyExisted() {
        return new ErrorResponse("400", "User's email address is already existed");
    }
}
