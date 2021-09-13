package com.codesoom.assignment.domain;

import com.codesoom.assignment.dto.ProductData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Product 클래스")
public class ProductTest {

    private Product product;
    private ProductData updateData;

    @BeforeEach
    void setUp() {

        product = Product.builder()
                .name("product1")
                .maker("maker1")
                .price(1000L)
                .imgUrl("img1")
                .build();

        updateData = ProductData.builder()
                .name("updateName")
                .maker("updateMaker")
                .price(5000L)
                .imgUrl("updateImg")
                .build();

    }

    @Test
    @DisplayName("객체가 올바르게 생성되는지 확인")
    void createProduct() {

        assertEquals("product1",product.getName());
        assertEquals("maker1",product.getMaker());
        assertEquals(1000l,product.getPrice());
        assertEquals("img1",product.getImgUrl());

    }

    @Test
    @DisplayName("ProductData 객체로 올바르게 수정되는지 확인")
    void updateProduct() {

        Product updateProduct = this.product.updateProduct(updateData);

        assertEquals("updateName", updateProduct.getName());
        assertEquals("updateMaker", updateProduct.getMaker());
        assertEquals(5000L, updateProduct.getPrice());
        assertEquals("updateImg", updateProduct.getImgUrl());

    }

}
