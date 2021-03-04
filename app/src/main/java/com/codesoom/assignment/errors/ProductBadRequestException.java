package com.codesoom.assignment.errors;

public class ProductBadRequestException extends RuntimeException{
    public ProductBadRequestException(String variable) {
        super("Product bad request: " + variable);
    }
}
