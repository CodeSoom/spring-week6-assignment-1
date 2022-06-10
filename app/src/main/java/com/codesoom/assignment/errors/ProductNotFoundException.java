package com.codesoom.assignment.errors;

/**
 * Product를 찾지 못했을 때 던진다.
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product not found: " + id);
    }
}
