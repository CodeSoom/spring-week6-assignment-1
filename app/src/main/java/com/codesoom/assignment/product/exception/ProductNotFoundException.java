package com.codesoom.assignment.product.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super(String.format("ID: %d 상품을 찾는데 실패하였습니다.", id));
    }
}
