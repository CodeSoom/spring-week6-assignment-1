package com.codesoom.assignment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("ProductRepository 인터페이스의")
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save {
        @Nested
        @DisplayName("상품이 주어지면")
        class Context_with_product {
            final Product givenProduct = Product.builder()
                    .name("장난감")
                    .maker("코드숨")
                    .price(3000)
                    .build();

            @Test
            @DisplayName("상품을 저장하고 리턴한다")
            void It_returns_product() {
                Product product = productRepository.save(givenProduct);

                assertThat(product.getName()).isEqualTo("장난감");
                assertThat(product.getMaker()).isEqualTo("코드숨");
                assertThat(product.getPrice()).isEqualTo(3000);
            }
        }
    }
}