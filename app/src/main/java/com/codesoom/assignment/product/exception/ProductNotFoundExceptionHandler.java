package com.codesoom.assignment.product.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductNotFoundExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductNotFoundExceptionHandler.class);

    @ExceptionHandler({ProductNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void productNotFoundException(Exception e) {
        LOGGER.error("error log = {}", e.toString());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void IllegalArgumentException(Exception e) {
        LOGGER.error("error log = {}", e.toString());
    }
}
