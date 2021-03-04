package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductBadRequestException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.errors.UserBadRequestException;
import com.codesoom.assignment.errors.UserEmailDuplicatedException;
import com.codesoom.assignment.errors.UserEmailNotExistedException;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerErrorAdvice {
    /** 상품을 찾을 수 없다는 메세지를 리턴한다. */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFound(ProductNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    /** 상품 생성 요청 정보가 잘못 되었다는 메세지를 리턴한다. */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleProductMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return new ErrorResponse(message);
    }

    /** 상품에 대한 요청이 잘못 되었다는 메세지를 리턴한다. */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductBadRequestException.class)
    public ErrorResponse handleProductBadRequest(ProductBadRequestException e) {
        return new ErrorResponse(e.getMessage());
    }

    /** 토큰 생성 정보가 비어있다는 메세지를 보낸다. */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ErrorResponse handleMissingRequestHeader(MissingRequestHeaderException e) {
        return new ErrorResponse(e.getMessage());
    }

    /** 사용자를 찾을 수 없다는 메세지를 리턴한다 */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound(UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    /** 사용자의 이메일이 중복되었다는 메세지를 리턴한다. */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicatedException.class)
    public ErrorResponse handleUserEmailDuplicated(UserEmailDuplicatedException e) {
        return new ErrorResponse(e.getMessage());
    }

    /** 사용자에 대한 요청이 잘못되었다는 메세지를 리턴한다. */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserBadRequestException.class)
    public ErrorResponse handleUserBadRequestException(UserBadRequestException e) {
        return new ErrorResponse(e.getMessage());
    }

    /** 토큰이 유효하지 않다는 메세지를 리턴한다. */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTokenException.class)
    public ErrorResponse handleInvalidTokenException(InvalidTokenException e) {
        return new ErrorResponse(e.getMessage());
    }

    /** 사용자 이메일이 존재하지 않다는 메세지를 리턴한다. */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailNotExistedException.class)
    public ErrorResponse handleUserEmailNotExistedException(UserEmailNotExistedException e) {
        return new ErrorResponse(e.getMessage());
    }
}
