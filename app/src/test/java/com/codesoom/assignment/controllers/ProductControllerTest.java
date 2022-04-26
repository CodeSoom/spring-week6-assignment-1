package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.product.ProductService;
import com.codesoom.assignment.controllers.product.ProductController;
import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.domain.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repository;


    @Nested
    @DisplayName("상품 삭제 요청은")
    class WhenProductRemoveRequest {

        @Nested
        @DisplayName("등록된 상품 존재한다면")
        class IfExistProduct {
            private Long productId;

            @BeforeEach
            void createProduct() {
                Product product = Product.builder()
                        .name("자바최적화")
                        .maker("히히히")
                        .price(Integer.valueOf(4000))
                        .build();
                this.productId = repository.save(product).getId();
            }

            @Test
            @DisplayName("상품을 삭제한다")
            void DoDeleteProduct() throws Exception {
                mockMvc.perform(delete("/products/" + productId))
                                .andExpect(status().isNoContent());
            }

        }

        @Nested
        @DisplayName("등록된 상품이 없다면")
        class IfDontAlreadyExistProduct {
            private final Long notExistId = 100L;

            @Test
            @DisplayName("예외를 반환한다")
            void throwIfNotExistException() throws Exception {
                mockMvc.perform(delete("/products/" + notExistId))
                        .andExpect(status().isNotFound());
            }

        }

    }

}
