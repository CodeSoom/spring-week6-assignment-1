package com.codesoom.assignment.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ProductData 클래스")
class ProductDataTest {

    @Test
    @DisplayName("올바르게 객체가 생성되는지 확인")
    void createProductData() {

        ProductData productData = ProductData.builder()
                .name("productData1")
                .maker("maker1")
                .price(1000L)
                .imageUrl("img1")
                .build();

        assertEquals("productData1",productData.getName());
        assertEquals("maker1",productData.getMaker());
        assertEquals(1000l,productData.getPrice());
        assertEquals("img1",productData.getImageUrl());

    }

}

