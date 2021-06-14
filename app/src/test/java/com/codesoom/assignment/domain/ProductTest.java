package com.codesoom.assignment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Product 도메인")
class ProductTest {
    private static final Long ID = 1L;
    private static final String NAME = "고양이 장난감";
    private static final String MAKER = "브랜드";
    private static final Integer PRICE = 9900;
    private static final String IMAGE_URL = "http://testimage.com/test";

    @Test
    @DisplayName("builder 테스트")
    void creationWithBuilder() {
        Product product = Product.builder()
                .id(ID)
                .name(NAME)
                .maker(MAKER)
                .price(PRICE)
                .imageUrl(IMAGE_URL)
                .build();

        assertThat(product.getId()).isEqualTo(ID);
        assertThat(product.getName()).isEqualTo(NAME);
        assertThat(product.getMaker()).isEqualTo(MAKER);
        assertThat(product.getPrice()).isEqualTo(PRICE);
        assertThat(product.getImageUrl()).isEqualTo(IMAGE_URL);
    }

    @Test
    @DisplayName("changeWith 메서드")
    void changeWith() {
        Product product = Product.builder()
                .id(1L)
                .name(NAME)
                .maker(MAKER)
                .price(PRICE)
                .build();

        product.changeWith(Product.builder()
                .name("쥐순이")
                .maker("코드숨")
                .price(10000)
                .build());

        assertThat(product.getName()).isEqualTo("쥐순이");
        assertThat(product.getMaker()).isEqualTo("코드숨");
        assertThat(product.getPrice()).isEqualTo(10000);
    }
}
