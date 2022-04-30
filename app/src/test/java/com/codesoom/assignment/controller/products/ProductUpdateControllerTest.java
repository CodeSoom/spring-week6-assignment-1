package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.application.auth.AuthorizationService;
import com.codesoom.assignment.application.products.ProductNotFoundException;
import com.codesoom.assignment.application.products.ProductUpdateService;
import com.codesoom.assignment.controller.products.ProductUpdateController;
import com.codesoom.assignment.domain.products.Product;
import com.codesoom.assignment.domain.products.ProductDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("ProductUpdateController 클래스")
@ActiveProfiles("test")
@SpringBootTest
public class ProductUpdateControllerTest {

    private ProductUpdateController controller;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ProductUpdateService service;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private ProductRepository repository;

    private String TOKEN;

    @BeforeEach
    void setup() {
        this.controller = new ProductUpdateController(service, authorizationService);
        this.TOKEN = jwtUtil.encode(1L);
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @DisplayName("updateProduct 메서드는")
    @Nested
    class Describe_update_product {

        private final ProductUpdateController.ProductUpdateDto productToUpdate
                = new ProductUpdateController.ProductUpdateDto(
                        "소쩍새", "유령회사", BigDecimal.valueOf(3000), "");

        @DisplayName("찾을 수 있는 상품 id가 주어지면")
        @Nested
        class Context_with_exist_id {

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                this.EXIST_ID = repository.save(Product.withoutId(
                        "쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "")).getId();
            }

            @DisplayName("수정된 상품을 반환한다.")
            @Test
            void will_return_updated_product() {
                Product product = controller.updateProduct(TOKEN, EXIST_ID, productToUpdate);

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
                assertThatThrownBy(() -> controller.updateProduct(TOKEN, NOT_EXIST_ID, productToUpdate))
                        .isInstanceOf(ProductNotFoundException.class);
            }
        }
    }

}
