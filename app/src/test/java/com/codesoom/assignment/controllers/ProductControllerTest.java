package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    private final String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyRW1haWwiOiJzZWRpbkBzZWRpbi5jb20ifQ.8VOB4g3s0uBkx3pNQnzI-LPg5DgdZs7yTizzbpIAvx0";
    private final String INVALID_BEARER_TOKEN = "invalid";
    private final String UPDATE_PREFIX = "업데이트";
    private final String PRODUCT_NAME = "쥐돌이";
    private final String PRODUCT_MAKER = "냥이월드";
    private final String USER_EMAIL = "sedin@sedin.com";
    private final int PRODUCT_PRICE = 5000;
    private final Long EXISTED_ID = 1L;
    private final Long NOT_EXISTED_ID = 1000L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product;

    @BeforeEach
    void prepare() {
        product = Product.builder()
                         .id(EXISTED_ID)
                         .name(PRODUCT_NAME)
                         .maker(PRODUCT_MAKER)
                         .price(PRODUCT_PRICE)
                         .build();

        given(authenticationService.parseToken(any(String.class))).willReturn(USER_EMAIL);
    }

    @Nested
    @DisplayName("GET /products 는")
    class Describe_list {

        @Nested
        @DisplayName("프로덕트가 있으면")
        class Context_with_product {

            @BeforeEach
            void prepareProduct() {
                given(productService.getProducts()).willReturn(Arrays.asList(product));
            }

            @Test
            @DisplayName("HttpStatus 200 OK를 응답한다")
            void it_returns_ok() throws Exception {
                mockMvc.perform(get("/products")
                               .accept(MediaType.APPLICATION_JSON_UTF8))
                       .andExpect(status().isOk())
                       .andExpect(content().string(containsString(PRODUCT_NAME)));

                verify(productService).getProducts();
            }
        }
    }

    @Nested
    @DisplayName("GET /products/{id} 는")
    class Describe_detail {

        @Nested
        @DisplayName("만약 존재하는 productId로 요청이 들어오면")
        class Context_with_existed_productId {

            @BeforeEach
            void prepareProduct() {
                given(productService.getProduct(EXISTED_ID)).willReturn(product);
            }

            @Test
            @DisplayName("HttpStatus 200 OK를 응답한다")
            void it_returns_ok() throws Exception {
                mockMvc.perform(get("/products/" + EXISTED_ID)
                               .accept(MediaType.APPLICATION_JSON_UTF8))
                       .andExpect(status().isOk())
                       .andExpect(content().string(containsString(PRODUCT_NAME)));

                verify(productService).getProduct(EXISTED_ID);
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않은 productId로 요청이 들어오면")
        class Context_with_not_existed_productId {

            @BeforeEach
            void prepareProduct() {
                given(productService.getProduct(NOT_EXISTED_ID)).willThrow(new ProductNotFoundException(NOT_EXISTED_ID));
            }

            @Test
            @DisplayName("HttpStatus 404 Not Found를 응답한다")
            void it_returns_not_found() throws Exception {
                mockMvc.perform(get("/products/" + NOT_EXISTED_ID))
                       .andExpect(status().isNotFound());

                verify(productService).getProduct(NOT_EXISTED_ID);
            }
        }
    }

    @Nested
    @DisplayName("POST /products 는")
    class Describe_create {

        @Nested
        @DisplayName("Jwt 토큰이 없거나 유효하지 않은 요청이 들어오면")
        class Context_with_not_existed_or_invalid_jwt_token {

            @Test
            @DisplayName("HttpStatus 400 Bad Request를 응답한다")
            void it_returns_bad_request() throws Exception {
                String content = objectMapper.writeValueAsString(ProductData.builder()
                                                                            .name("")
                                                                            .maker("")
                                                                            .price(0)
                                                                            .build());

                mockMvc.perform(post("/products")
                               .accept(MediaType.APPLICATION_JSON_UTF8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                       .andExpect(status().isBadRequest());

                mockMvc.perform(post("/products")
                               .header("Authorization", BEARER_TOKEN)
                               .accept(MediaType.APPLICATION_JSON_UTF8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                       .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("Jwt 토큰이 유효하고 프로덕트 생성 요청이 들어오면")
        class Context_with_valid_jwt_token_and_product_data {

            @BeforeEach
            void prepareProduct() {
                given(productService.createProduct(any(ProductData.class))).willReturn(product);
            }

            @Test
            @DisplayName("HttpStatus 201 Created를 응답한다")
            void it_returns_created() throws Exception {
                String content = objectMapper.writeValueAsString(ProductData.builder()
                                                                            .name(PRODUCT_NAME)
                                                                            .maker(PRODUCT_MAKER)
                                                                            .price(PRODUCT_PRICE)
                                                                            .build());

                mockMvc.perform(post("/products")
                               .header("Authorization", BEARER_TOKEN)
                               .accept(MediaType.APPLICATION_JSON_UTF8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                       .andExpect(status().isCreated())
                       .andExpect(content().string(containsString(PRODUCT_NAME)));

                verify(productService).createProduct(any(ProductData.class));
            }
        }

        @Nested
        @DisplayName("Jwt 토큰이 유효하고 프로덕트 정보가 유효하지 않은 요청이 들어오면")
        class Context_with_valid_jwt_token_and_invalid_product_data {

            @Test
            @DisplayName("HttpStatus 400 Bad Request를 응답한다")
            void it_returns_bad_request() throws Exception {
                String content = objectMapper.writeValueAsString(ProductData.builder()
                                                                            .name("")
                                                                            .maker("")
                                                                            .price(0)
                                                                            .build());

                mockMvc.perform(post("/products")
                               .header("Authorization", BEARER_TOKEN)
                               .accept(MediaType.APPLICATION_JSON_UTF8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                       .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("PATCH /products/{id} 는")
    class Describe_update {

        @Nested
        @DisplayName("Jwt 토큰이 없거나 유효하지 않은 요청이 들어오면")
        class Context_with_not_existed_or_invalid_jwt_token {

            @Test
            @DisplayName("HttpStatus 400 Bad Request를 응답한다")
            void it_returns_bad_request() throws Exception {
                String content = objectMapper.writeValueAsString(ProductData.builder()
                                                                            .name("")
                                                                            .maker("")
                                                                            .price(0)
                                                                            .build());

                mockMvc.perform(patch("/products/" + EXISTED_ID)
                               .accept(MediaType.APPLICATION_JSON_UTF8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                       .andExpect(status().isBadRequest());

                mockMvc.perform(patch("/products/" + EXISTED_ID)
                               .header("Authorization", BEARER_TOKEN)
                               .accept(MediaType.APPLICATION_JSON_UTF8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                       .andExpect(status().isBadRequest());

            }
        }

        @Nested
        @DisplayName("Jwt 토큰이 유효하고 존재하는 productId와 정보로 요청이 들어오면")
        class Context_with_valid_jwt_token_and_productId_and_productData {

            @BeforeEach
            void prepareProduct() {
                given(productService.updateProduct(eq(EXISTED_ID), any(ProductData.class)))
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
            @DisplayName("HttpStatus 200 OK를 응답한다")
            void it_returns_ok() throws Exception {
                String content = objectMapper.writeValueAsString(ProductData.builder()
                                                                            .name(UPDATE_PREFIX + PRODUCT_NAME)
                                                                            .maker(PRODUCT_MAKER)
                                                                            .price(PRODUCT_PRICE)
                                                                            .build());

                mockMvc.perform(patch("/products/" + EXISTED_ID)
                               .header("Authorization", BEARER_TOKEN)
                               .accept(MediaType.APPLICATION_JSON_UTF8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                       .andExpect(status().isOk())
                       .andExpect(content().string(containsString(UPDATE_PREFIX + PRODUCT_NAME)));

                verify(productService).updateProduct(eq(EXISTED_ID), any(ProductData.class));
            }
        }

        @Nested
        @DisplayName("Jwt 토큰이 유효하고 존재하지 않은 productId로 요청이 들어오면")
        class Context_with_valid_jwt_token_and_not_existed_productId {

            @BeforeEach
            void prepare() {
                given(productService.updateProduct(eq(NOT_EXISTED_ID), any(ProductData.class))).willThrow(new ProductNotFoundException(NOT_EXISTED_ID));
            }

            @Test
            @DisplayName("HttpStatus 404 Not Found를 응답한다")
            void it_returns_not_found() throws Exception {
                String content = objectMapper.writeValueAsString(ProductData.builder()
                                                                            .name(UPDATE_PREFIX + PRODUCT_NAME)
                                                                            .maker(PRODUCT_MAKER)
                                                                            .price(PRODUCT_PRICE)
                                                                            .build());

                mockMvc.perform(patch("/products/" + NOT_EXISTED_ID)
                               .header("Authorization", BEARER_TOKEN)
                               .accept(MediaType.APPLICATION_JSON_UTF8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                       .andExpect(status().isNotFound());

                verify(productService).updateProduct(eq(NOT_EXISTED_ID), any(ProductData.class));
            }
        }

        @Nested
        @DisplayName("Jwt 토큰과 productId가 존재하고 프로덕트 정보가 유효하지 않은 요청이 들어오면")
        class Context_with_valid_jwt_token_and_productId_and_invalid_productData {

            @Test
            @DisplayName("HttpStatus 400 Bad Request를 응답한다")
            void it_returns_bad_request() throws Exception {
                String content = objectMapper.writeValueAsString(ProductData.builder()
                                                                            .name("")
                                                                            .maker("")
                                                                            .price(0)
                                                                            .build());

                mockMvc.perform(patch("/products/" + EXISTED_ID)
                               .accept(MediaType.APPLICATION_JSON_UTF8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                       .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("DELETE /products/{id} 는")
    class Describe_destroy {

        @Nested
        @DisplayName("Jwt 토큰이 없거나 유효하지 않은 요청이 들어오면")
        class Context_with_not_existed_or_invalid_jwt_token {

            @BeforeEach
            void prepare() {
                given(authenticationService.parseToken(any(String.class))).willThrow(new InvalidTokenException(any(String.class)));
            }

            @Test
            @DisplayName("HttpStatus 400 Bad Request를 응답한다")
            void it_returns_bad_request() throws Exception {
                mockMvc.perform(delete("/products/" + EXISTED_ID))
                       .andExpect(status().isBadRequest());

                mockMvc.perform(delete("/products/" + EXISTED_ID)
                               .header("Authorization", INVALID_BEARER_TOKEN))
                       .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("Jwt 토큰이 유효하고 존재하는 productId로 요청이 들어오면")
        class Context_with_valid_jwt_token_and_product_id {

            @BeforeEach
            void prepare() {
                given(productService.deleteProduct(EXISTED_ID)).willReturn(product);
            }

            @Test
            @DisplayName("HttpStatus 204 No Content를 응답한다")
            void it_returns_no_content() throws Exception {
                mockMvc.perform(delete("/products/" + EXISTED_ID)
                               .header("Authorization", BEARER_TOKEN))
                       .andExpect(status().isNoContent());

                verify(productService).deleteProduct(EXISTED_ID);
            }
        }

        @Nested
        @DisplayName("Jwt 토큰이 유효하고 존재하지 않은 productId로 요청이 들어오면")
        class Context_with_valid_jwt_token_and_not_existed_product_id {

            @BeforeEach
            void prepare() {
                given(productService.deleteProduct(NOT_EXISTED_ID)).willThrow(new ProductNotFoundException(NOT_EXISTED_ID));
            }

            @Test
            @DisplayName("HttpStatus 404 Not Found를 응답한다")
            void it_returns_not_found() throws Exception {
                mockMvc.perform(delete("/products/" + NOT_EXISTED_ID)
                               .header("Authorization", BEARER_TOKEN))
                       .andExpect(status().isNotFound());

                verify(productService).deleteProduct(NOT_EXISTED_ID);
            }
        }
    }
}
