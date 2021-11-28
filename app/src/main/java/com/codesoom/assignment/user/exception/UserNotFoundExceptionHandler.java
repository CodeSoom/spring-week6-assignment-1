package com.codesoom.assignment.user.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserNotFoundExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserNotFoundException.class);

    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void UserNotFoundException(UserNotFoundException e) {
        LOGGER.error("error log = {}", e.toString());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void MethodArgumentNotValidException(IllegalArgumentException e) {
        LOGGER.error("error log = {}", e.toString());
    }
}
