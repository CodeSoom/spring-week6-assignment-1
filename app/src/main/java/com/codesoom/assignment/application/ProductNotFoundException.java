package com.codesoom.assignment.application;


import org.springframework.http.HttpStatus;

/** 상품을 찾지 못하면 던집니다. */
public class ProductNotFoundException extends RuntimeException{

    private final String code;

    private ProductNotFoundException(String code, Long id) {
        super(String.format("%s로 해당하는 상품을 찾을 수 없습니다.", id));
        this.code = code;
    }

    public ProductNotFoundException(Long id) {
        this(String.valueOf(HttpStatus.NOT_FOUND), id);
    }

    private ProductNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ProductNotFoundException(String message) {
        this(String.valueOf(HttpStatus.NOT_FOUND), message);
    }

    public String getCode() {
        return code;
    }

}
