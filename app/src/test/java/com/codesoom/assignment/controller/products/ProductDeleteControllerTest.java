package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.application.auth.AuthorizationService;
import com.codesoom.assignment.application.products.ProductDeleteService;
import com.codesoom.assignment.application.products.ProductNotFoundException;
import com.codesoom.assignment.controller.products.ProductDeleteController;
import com.codesoom.assignment.domain.products.Product;
import com.codesoom.assignment.domain.products.ProductRepository;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("ProductDeleteController 클래스")
@ActiveProfiles("test")
@SpringBootTest
public class ProductDeleteControllerTest {

    private ProductDeleteController controller;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ProductDeleteService service;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private ProductRepository repository;

    private String TOKEN;

    @BeforeEach
    void setup() {
        this.controller = new ProductDeleteController(service, authorizationService);
        this.TOKEN = jwtUtil.encode(1L);
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @DisplayName("deleteProduct 메서드는")
    @Nested
    class Describe_delete_product {

        @DisplayName("찾을 수 있는 상품의 id가 주어지면")
        @Nested
        class Context_with_exist_id {

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                final Product product
                        = Product.withoutId("쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "");
                this.EXIST_ID = repository.save(product).getId();
            }

            @DisplayName("해당 상품을 삭제한다.")
            @Test
            void it_delete_product() {
                controller.deleteProduct(TOKEN, EXIST_ID);
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
                assertThatThrownBy(() -> controller.deleteProduct(TOKEN, NOT_EXIST_ID))
                        .isInstanceOf(ProductNotFoundException.class);
            }
        }
    }
}
