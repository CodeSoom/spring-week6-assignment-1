package com.codesoom.assignment.errors;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("입력하신 상품 ID["+id+"]에 대한 결과가 존재하지 않습니다");
    }
}
