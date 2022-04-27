package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.application.products.ProductSaveService;
import com.codesoom.assignment.domain.products.Product;
import com.codesoom.assignment.domain.products.ProductRepository;
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


@DisplayName("ProductSaveController 클래스")
@ActiveProfiles("test")
@SpringBootTest
public class ProductSaveControllerTest {

    private ProductSaveController controller;

    @Autowired
    private ProductSaveService service;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setup() {
        this.controller = new ProductSaveController(service);
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @DisplayName("saveProduct 메서드는")
    @Nested
    class Describe_save_product {

        @DisplayName("필수값을 모두 입력하면")
        @Nested
        class Context_with_valid_data {

            private final ProductSaveController.ProductSaveDto VALID_PRODUCT_DTO
                    = new ProductSaveController.ProductSaveDto(
                            "쥐돌이", "냥이월드", BigDecimal.valueOf(2000), "url");

            @AfterEach
            void cleanup() {
                repository.deleteAll();
            }

            @DisplayName("상품을 성공적으로 등록한다.")
            @Test
            void it_will_save_product() {
                Product product = controller.saveProduct(VALID_PRODUCT_DTO);

                assertThat(product.getName()).isEqualTo(VALID_PRODUCT_DTO.getName());
                assertThat(repository.findById(product.getId())).isNotEmpty();
            }
        }
    }

}
