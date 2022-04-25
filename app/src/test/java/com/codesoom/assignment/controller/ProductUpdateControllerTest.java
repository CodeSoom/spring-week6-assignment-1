package com.codesoom.assignment.controller;

import com.codesoom.assignment.application.ProductNotFoundException;
import com.codesoom.assignment.application.ProductUpdateService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductDto;
import com.codesoom.assignment.domain.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("ProductUpdateController 클래스")
@ActiveProfiles("test")
@SpringBootTest
public class ProductUpdateControllerTest {

    private ProductUpdateController controller;

    @Autowired
    private ProductUpdateService service;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setup() {
        this.controller = new ProductUpdateController(service);
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @DisplayName("updateProduct 메서드는")
    @Nested
    class Describe_update_product {

        private final ProductDto productToUpdate
                = new ProductDto("소쩍새", "유령회사", BigDecimal.valueOf(3000), "");

        @DisplayName("찾을 수 있는 상품 id가 주어지면")
        @Nested
        class Context_with_exist_id {

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                this.EXIST_ID = repository.save(
                        new Product("쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "")).getId();
            }

            @DisplayName("수정된 상품을 반환한다.")
            @Test
            void will_return_updated_product() {
                Product product = controller.updateProduct(EXIST_ID, productToUpdate);

                assertThat(product.getId()).isEqualTo(EXIST_ID);
                assertThat(product.getName()).isEqualTo(productToUpdate.getName());
            }
        }

        @DisplayName("찾을 수 없는 상품 id가 주어지면")
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
            void will_return_updated_product() {
                assertThatThrownBy(() -> controller.updateProduct(NOT_EXIST_ID, productToUpdate))
                        .isInstanceOf(ProductNotFoundException.class);
            }
        }
    }

}
