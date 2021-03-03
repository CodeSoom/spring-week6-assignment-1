package com.codesoom.assignment.errors;

public class ProductBadRequestException extends RuntimeException{
    public ProductBadRequestException(String variable) {
        super(String.format("%s 값은 필수입니다", variable));
    }
}
