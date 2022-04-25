package com.codesoom.assignment.application.products;

import com.codesoom.assignment.application.ServiceTest;
import com.codesoom.assignment.application.products.ProductCommandService;
import com.codesoom.assignment.application.products.ProductNotFoundException;
import com.codesoom.assignment.domain.products.Product;
import com.codesoom.assignment.domain.products.ProductDto;
import com.codesoom.assignment.domain.products.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class ProductUpdateTest extends ServiceTest {

    private ProductCommandService service;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setup() {
        this.service = new ProductCommandService(repository);
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @DisplayName("updateProduct 메서드는")
    @Nested
    class Describe_update_product {

        private final ProductDto PRODUCT_DTO
                = new ProductDto("꿈돌이", "유령회사", BigDecimal.valueOf(5000), "");

        @DisplayName("찾을 수 있는 상품의 id가 주어지면")
        @Nested
        class Context_with_exist_id {

            private Long EXIST_ID;
            private final Product OLD_PRODUCT
                    = new Product("쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "");

            @BeforeEach
            void setup() {
                final Product product = repository.save(OLD_PRODUCT);
                this.EXIST_ID = product.getId();
            }

            @AfterEach
            void cleanup() {
                repository.deleteAll();
            }

            @DisplayName("상품을 변경한 뒤, 변경 결과를 반환한다.")
            @Test
            void will_return_updated_product() {
               final Product product = service.updateProduct(EXIST_ID, PRODUCT_DTO);

                assertThat(product.getId()).isEqualTo(EXIST_ID);
                assertThat(product.getName()).isNotEqualTo(OLD_PRODUCT.getName());
            }
        }

        @DisplayName("찾을 수 없는 상품의 id가 주어지면")
        @Nested
        class Context_with_not_exist_id {

            private final Long NOT_EXIST_ID = 100L;

            @BeforeEach
            void setup() {
                if (repository.existsById(NOT_EXIST_ID)) {
                    repository.deleteById(NOT_EXIST_ID);
                }
            }

            @DisplayName("예외를 던진다.")
            @Test
            void will_throw_exception() {
                assertThatThrownBy(() -> service.updateProduct(NOT_EXIST_ID, PRODUCT_DTO))
                        .isInstanceOf(ProductNotFoundException.class);
            }
        }
    }

}
