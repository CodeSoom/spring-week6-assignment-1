package com.codesoom.assignment.errors;

import java.util.NoSuchElementException;

public class ProductNotFoundException extends NoSuchElementException {

    public ProductNotFoundException() {

        super("조회하려는 상품을 찾을 수 없습니다.");

    }

}

