package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 공통 예외 처리 핸들러
 */
@ResponseBody
@ControllerAdvice
public class ControllerCommonErrorAdvice {
    /**
     * MissingRequestHeaderException 예외 발생시, UNAUTHORIZED 응답코드를 반환합니다.
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleMissingRequestHeaderException() {
        return new ErrorResponse("Product not found");
    }
}
