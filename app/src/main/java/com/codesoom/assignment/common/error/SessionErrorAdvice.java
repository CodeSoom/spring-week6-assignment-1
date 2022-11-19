package com.codesoom.assignment.common.error;

import com.codesoom.assignment.product.presentation.ProductController;
import com.codesoom.assignment.session.exception.InvalidTokenException;
import com.codesoom.assignment.session.exception.TokenNotExistException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = {
        ProductController.class
})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SessionErrorAdvice {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTokenException.class)
    public ErrorResponse handleInvalidToken() {
        return new ErrorResponse("토큰이 유효하지 않습니다");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenNotExistException.class)
    public ErrorResponse handleTokenNotExist() {
        return new ErrorResponse("토큰이 존재하지 않습니다");
    }
}
