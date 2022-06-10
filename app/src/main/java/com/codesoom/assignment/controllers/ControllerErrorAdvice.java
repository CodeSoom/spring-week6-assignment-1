package com.codesoom.assignment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.exception.DecodingInValidTokenException;
import com.codesoom.assignment.exception.ProductNotFoundException;
import com.codesoom.assignment.exception.UserEmailDuplicationException;
import com.codesoom.assignment.exception.UserNotFoundException;

@ResponseBody
@ControllerAdvice
public class ControllerErrorAdvice {
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
	@ExceptionHandler(DecodingInValidTokenException.class)
	public ErrorResponse handleUserDecodingInValidTokenException() {
		return new ErrorResponse("token is invalid");
	}
}
