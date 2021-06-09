package com.codesoom.assignment.dto;

import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class ProductDataTest {
    @ParameterizedTest
    @AutoSource
    void creationWithBuilder(
            Long id,
            String name,
            String maker,
            Integer price,
            String imageUrl) {
        var productData = ProductData.builder()
                                     .id(id)
                                     .name(name)
                                     .maker(maker)
                                     .price(price)
                                     .imageUrl(imageUrl)
                                     .build();

        assertThat(productData.getId()).isEqualTo(id);
        assertThat(productData.getName()).isEqualTo(name);
        assertThat(productData.getMaker()).isEqualTo(maker);
        assertThat(productData.getPrice()).isEqualTo(price);
        assertThat(productData.getImageUrl()).isEqualTo(imageUrl);
    }
}
