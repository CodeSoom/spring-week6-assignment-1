package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.application.products.ProductNotFoundException;
import com.codesoom.assignment.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 상품 컨트롤러에서 던지는 예외를 받아 응답합니다.
 */
@RestControllerAdvice(annotations = ProductController.class)
public class ProductControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFoundException(ProductNotFoundException e) {
        return ErrorResponse.of(e.getCode(), e.getMessage());
    }

}
