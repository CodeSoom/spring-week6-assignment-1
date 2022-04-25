package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.application.products.ProductNotFoundException;
import com.codesoom.assignment.application.products.ProductReadServiceImpl;
import com.codesoom.assignment.controller.products.ProductReadController;
import com.codesoom.assignment.domain.products.Product;
import com.codesoom.assignment.domain.products.ProductRepository;
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


@DisplayName("ProductReadController 클래스")
@ActiveProfiles("test")
@SpringBootTest
public class ProductReadControllerTest {

    private ProductReadController controller;

    @Autowired
    private ProductReadServiceImpl service;

    @Autowired
    private ProductRepository repository;

    private static final Product SAVED_PRODUCT
            = new Product("쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "");

    @BeforeEach
    void setup() {
        this.controller = new ProductReadController(service);
        repository.deleteAll();
    }

    @DisplayName("getProducts 메서드는")
    @Nested
    class Describe_get_products {
        @BeforeEach
        void setup() {
            repository.save(SAVED_PRODUCT);
        }

        @DisplayName("전체 상품을 성공적으로 조회한다.")
        @Test
        void findAllTest() {
            assertThat(controller.getProducts()).isNotEmpty();
        }
    }

    @DisplayName("getProduct 메서드는")
    @Nested
    class Describe_get_product {

        @DisplayName("찾을 수 있는 상품의 id가 주어지면")
        @Nested
        class Context_with_exist_id {

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                this.EXIST_ID = repository.save(SAVED_PRODUCT).getId();
            }

            @DisplayName("해당 상품을 반환한다.")
            @Test
            void will_return_found_product() {
                assertThat(controller.getProduct(EXIST_ID)).isNotNull();
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
            void will_throw_not_found_exception() {
                assertThatThrownBy(() -> controller.getProduct(NOT_EXIST_ID))
                        .isInstanceOf(ProductNotFoundException.class);
            }
        }
    }

}
