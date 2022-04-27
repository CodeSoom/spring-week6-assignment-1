package com.codesoom.assignment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Produt 엔티티 에서")
class ProductTest {
    private static final Integer PRODUCT_PRICE = 1000;
    private static final String PRODUCT_NAME = "상품1";
    private static final String PRODUCT_MAKER = "maker";
    private static final String PRODUCT_IMAGE_URL = "imageUrl";

    private static final Integer CHANGE_PRODUCT_PRICE = 700;
    private static final String CHANGE_PRODUCT_NAME = "상품100";
    private static final String CHANGE_PRODUCT_MAKER = "maker100";
    private static final String CHANGE_PRODUCT_IMAGE_URL = "changedImageUrl";

    @Test
    @DisplayName("of() 메소드를 사용하여 Product를 생성할 때")
    void creationWithBuilder() {
        Product product = Product.of(
                PRODUCT_NAME,
                PRODUCT_MAKER,
                PRODUCT_PRICE,
                PRODUCT_IMAGE_URL
        );

        assertThat(product.getName()).isEqualTo(PRODUCT_NAME);
        assertThat(product.getMaker()).isEqualTo(PRODUCT_MAKER);
        assertThat(product.getPrice()).isEqualTo(PRODUCT_PRICE);
        assertThat(product.getImageUrl()).isEqualTo(PRODUCT_IMAGE_URL);
    }

    @Test
    @DisplayName("chnage 메소드로 데이터를 변경할 때")
    void change() {
        Product product = Product.of(
                PRODUCT_NAME,
                PRODUCT_MAKER,
                PRODUCT_PRICE,
                PRODUCT_IMAGE_URL
        );

        product.change(
                CHANGE_PRODUCT_NAME,
                CHANGE_PRODUCT_MAKER,
                CHANGE_PRODUCT_PRICE,
                CHANGE_PRODUCT_IMAGE_URL
        );

        assertThat(product.getName()).isEqualTo(CHANGE_PRODUCT_NAME);
        assertThat(product.getMaker()).isEqualTo(CHANGE_PRODUCT_MAKER);
        assertThat(product.getPrice()).isEqualTo(CHANGE_PRODUCT_PRICE);
        assertThat(product.getImageUrl()).isEqualTo(CHANGE_PRODUCT_IMAGE_URL);
    }
}
