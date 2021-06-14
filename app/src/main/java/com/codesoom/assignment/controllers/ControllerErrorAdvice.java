package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.PasswordNotCorrectException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.errors.UserEmailDuplicationException;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 컨트롤러 관련 예외처리를 담당합니다.
 */
@ResponseBody
@ControllerAdvice
public class ControllerErrorAdvice {

    /**
     * 상품을 찾지 못 했을때 에러 응답을 리턴합니다.
     * @return 에러 응답
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFound() {
        return new ErrorResponse("Product not found");
    }

    /**
     * 사용자를 찾지 못 했을때 에러 응답을 리턴합니다.
     * @return 에러 응답
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound() {
        return new ErrorResponse("User not found");
    }

    /**
     * 사용자 이메일이 중복일때 에러 응답을 리턴합니다.
     * @return 에러 응답
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicationException.class)
    public ErrorResponse handleUserEmailIsAlreadyExisted() {
        return new ErrorResponse("User's email address is already existed");
    }

    /**
     * 사용자 비밀번호가 틀렸을때 에러 응답을 리턴합니다.
     * @return 에러 응답
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordNotCorrectException.class)
    public ErrorResponse handlePasswordNotCorrect() {
        return new ErrorResponse("Password Not Correct");
    }

    /**
     * 토큰이 유효하지 않을때 에러 응답을 리턴합니다.
     * @return 에러 응답
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidTokenException.class)
    public ErrorResponse handleInvalidToken() {
        return new ErrorResponse("Invalid token");
    }
}
