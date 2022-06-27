package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.errors.VerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthService authService;

    private final Product product = Product.builder()
            .id(1L)
            .name("쥐돌이")
            .maker("냥이월드")
            .price(5000)
            .build();

    @BeforeEach
    void setUp() {
        reset(authService);
    }

    @Nested
    @DisplayName("GET /products 요청은")
    class Describe_list {

        @BeforeEach
        void setUp() {
            given(productService.getProducts()).willReturn(List.of(product));
        }

        @Test
        @DisplayName("product list를 응답한다.")
        void it_responses_product_list() throws Exception {
            mockMvc.perform(
                            get("/products")
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("쥐돌이")));
        }
    }

    @Nested
    @DisplayName("GET /products/{id} 요청은")
    class Describe_detail {

        @Nested
        @DisplayName("존재하는 product가 주어지면")
        class Context_with_existed_product {

            @BeforeEach
            void setUp() {
                given(productService.getProduct(1L)).willReturn(product);
            }

            @Test
            @DisplayName("product를 응답한다.")
            void it_responses_product() throws Exception {
                mockMvc.perform(
                                get("/products/1")
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("쥐돌이")));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 product가 주어지면")
        class Context_with_non_existed_product {

            @BeforeEach
            void setUp() {
                given(productService.getProduct(1000L))
                        .willThrow(new ProductNotFoundException(1000L));
            }

            @Test
            @DisplayName("404 status를 응답한다.")
            void it_returns_404_status() throws Exception {
                mockMvc.perform(get("/products/1000"))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("POST /products 요청은")
    class Describe_create {

        @Nested
        @DisplayName("유효한 요청 값들이오면")
        class Context_with_valid_attributes {

            @BeforeEach
            void setUp() {
                given(productService.createProduct(any(ProductData.class)))
                        .willReturn(product);
            }

            @Test
            @DisplayName("생성된 product를 응답한다.")
            void it_responses_created_product() throws Exception {
                mockMvc.perform(
                                post("/products")
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                                "\"price\":5000}")
                        )
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString("쥐돌이")));

                verify(productService).createProduct(any(ProductData.class));
                verify(authService).verify(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 요청 값들이오면")
        class Context_with_invalid_attributes {

            @BeforeEach
            void setUp() {
                given(productService.createProduct(any(ProductData.class)))
                        .willReturn(product);
            }

            @Test
            @DisplayName("400 status를 응답한다.")
            void it_responses_400_status() throws Exception {
                mockMvc.perform(
                                post("/products")
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"\",\"maker\":\"\"," +
                                                "\"price\":0}")
                        )
                        .andExpect(status().isBadRequest());

                verify(authService).verify(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 인증 토큰이 주어지면")
        class Context_with_invalid_authorization_token {

            @BeforeEach
            void setUp() {
                given(authService.verify(INVALID_TOKEN))
                        .willThrow(VerificationException.class);
            }

            @Test
            @DisplayName("401 status를 응답한다.")
            void it_responses_401_status() throws Exception {
                mockMvc.perform(
                                post("/products")
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + INVALID_TOKEN)
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                                "\"price\":5000}")
                        )
                        .andExpect(status().isUnauthorized());

                verify(authService).verify(INVALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("PATCH /products/{id} 요청은")
    class Describe_update {

        @Nested
        @DisplayName("존재하는 product가 주어지면")
        class Context_with_existed_product {

            @BeforeEach
            void setUp() {
                given(productService.updateProduct(eq(1L), any(ProductData.class)))
                        .will(invocation -> {
                            Long id = invocation.getArgument(0);
                            ProductData productData = invocation.getArgument(1);
                            return Product.builder()
                                    .id(id)
                        .name(productData.getName())
                                    .maker(productData.getMaker())
                                    .price(productData.getPrice())
                                    .build();
                        });
            }

            @Test
            @DisplayName("변경된 product를 응답한다.")
            void it_responses_updated_product() throws Exception {
                mockMvc.perform(
                                patch("/products/1")
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                                "\"price\":5000}")
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("쥐순이")));

                verify(productService).updateProduct(eq(1L), any(ProductData.class));
                verify(authService).verify(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 product가 주어지면")
        class Context_with_non_existed_product {

            @BeforeEach
            void setUp() {
                given(productService.updateProduct(eq(1000L), any(ProductData.class)))
                        .willThrow(new ProductNotFoundException(1000L));
            }

            @Test
            @DisplayName("404 status를 응답한다.")
            void it_returns_404_status() throws Exception {
                mockMvc.perform(
                                patch("/products/1000")
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                                "\"price\":5000}")
                        )
                        .andExpect(status().isNotFound());

                verify(productService).updateProduct(eq(1000L), any(ProductData.class));
                verify(authService).verify(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 요청 값들이오면")
        class Context_with_invalid_attributes {

            @Test
            @DisplayName("400 status를 응답한다.")
            void it_responses_400_status() throws Exception {
                mockMvc.perform(
                                patch("/products/1")
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"\",\"maker\":\"\"," +
                                                "\"price\":0}")
                        )
                        .andExpect(status().isBadRequest());

                verify(authService).verify(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 인증 토큰이 주어지면")
        class Context_with_invalid_authorization_token {

            @BeforeEach
            void setUp() {
                given(authService.verify(INVALID_TOKEN))
                        .willThrow(VerificationException.class);
            }

            @Test
            @DisplayName("401 status를 응답한다.")
            void it_responses_401_status() throws Exception {
                mockMvc.perform(
                                patch("/products/1000")
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + INVALID_TOKEN)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                                "\"price\":5000}")
                        )
                        .andExpect(status().isUnauthorized());

                verify(authService).verify(INVALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("DELETE /products 요청은")
    class Describe_destroy {

        @Nested
        @DisplayName("존재하는 product가 주어지면")
        class Context_with_existed_product {

            @Test
            @DisplayName("204 상태코드를 응답한다.")
            void it_responses_204_status() throws Exception {
                mockMvc.perform(
                                delete("/products/1")
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                        )
                        .andExpect(status().isNoContent());

                verify(productService).deleteProduct(1L);
                verify(authService).verify(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 product가 주어지면")
        class Context_with_non_existed_product {

            @BeforeEach
            void setUp() {
                given(productService.deleteProduct(1000L))
                        .willThrow(new ProductNotFoundException(1000L));
            }

            @Test
            @DisplayName("404 status를 응답한다.")
            void it_returns_404_status() throws Exception {
                mockMvc.perform(
                                delete("/products/1000")
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                        )
                        .andExpect(status().isNotFound());

                verify(productService).deleteProduct(1000L);
                verify(authService).verify(VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("유효하지 않은 인증 토큰이 주어지면")
    class Context_with_invalid_authorization_token {

        @BeforeEach
        void setUp() {
            given(authService.verify(INVALID_TOKEN))
                    .willThrow(VerificationException.class);
        }

        @Test
        @DisplayName("201 상태코드를 응답한다.")
        void it_responses_201_status() throws Exception {
            mockMvc.perform(
                            delete("/products/1")
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + INVALID_TOKEN)
                    )
                    .andExpect(status().isUnauthorized());

            verify(authService).verify(INVALID_TOKEN);
        }
    }

}
