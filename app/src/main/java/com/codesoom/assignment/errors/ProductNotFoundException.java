package com.codesoom.assignment.errors;

/**
 * 상품 탐색 실패 예외.
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product not found: " + id);
    }
}
