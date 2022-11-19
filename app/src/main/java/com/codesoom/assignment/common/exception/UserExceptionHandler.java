package com.codesoom.assignment.common.exception;

import com.codesoom.assignment.session.controller.SessionController;
import com.codesoom.assignment.user.exception.UserEmailDuplicationException;
import com.codesoom.assignment.user.exception.UserInvalidPasswordException;
import com.codesoom.assignment.user.exception.UserNotFoundException;
import com.codesoom.assignment.user.presentation.UserController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = {
        UserController.class,
        SessionController.class
})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserExceptionHandler {

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

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserInvalidPasswordException.class)
    public ErrorResponse handleUserInvalidPassword(UserInvalidPasswordException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
