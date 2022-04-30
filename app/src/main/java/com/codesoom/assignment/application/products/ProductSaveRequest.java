package com.codesoom.assignment.application.products;

import com.codesoom.assignment.domain.products.Product;

import java.math.BigDecimal;

/**
 * 상품 정보 저장 요청.
 */
public interface ProductSaveRequest {

    String getName();

    String getMaker();

    BigDecimal getPrice();

    String getImageUrl();

    default Product product() {
        return Product.withoutId(getName(), getMaker(), getPrice(), getImageUrl());
    }

}
