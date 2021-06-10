package com.codesoom.assignment.controllers;

import com.codesoom.assignment.EncodingConfig;
import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.dto.SessionTokenData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@DisplayName("ProductControllerApi의")
@SpringBootTest
@AutoConfigureMockMvc
@EncodingConfig
class ProductControllerApiTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";
    private static final String SECRET = "12345678901234567890123456789010";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    private ObjectMapper objectMapper;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        this.jwtUtil = new JwtUtil(SECRET);
        this.objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("list 메소드는")
    class DescribeList {

        @Nested
        @DisplayName("상품 목록에 대한 요청을 받으면")
        class ContextWithProducts {

            private Product product;

            @BeforeEach
            void setUp() {
                product = Product.builder()
                        .id(1L)
                        .name("쥐돌이")
                        .maker("냥이월드")
                        .price(5000)
                        .build();
                given(productService.getProducts()).willReturn(List.of(product));
            }

            @Test
            @DisplayName("Ok status와 상품 목록을 반환합니다")
            void ItReturnsOkWithProductList() throws Exception {
                String expected = objectMapper.writeValueAsString(List.of(product));
                mockMvc.perform(
                        get("/products")
                                .accept(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isOk())
                        .andExpect(content().json(expected, true));
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class DescribeDetail {

        @Nested
        @DisplayName("존재하는 상품에 대한 요청을 받으면")
        class ContextWithExistedProduct {

            private Product product;

            @BeforeEach
            void setUp() {
                product = Product.builder()
                        .id(1L)
                        .name("쥐돌이")
                        .maker("냥이월드")
                        .price(5000)
                        .build();
                given(productService.getProduct(1L))
                        .willReturn(product);
            }

            @Test
            @DisplayName("Ok status와 상품을 반환합니다")
            void ItReturnsOkWithProduct() throws Exception {
                String expected = objectMapper.writeValueAsString(product);
                mockMvc.perform(
                        get("/products/1")
                                .accept(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isOk())
                        .andExpect(content().json(expected, true));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 상품에 대한 요청을 받으면")
        class ContextWithNotExistedProduct {

            @BeforeEach
            void setUp() {
                given(productService.getProduct(1000L))
                        .willThrow(ProductNotFoundException.class);
            }

            @Test
            @DisplayName("NotFound status를 반환합니다")
            void ItReturnsNotFound() throws Exception {
                mockMvc.perform(get("/products/1000"))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class DescribeCrete {

        private String authorizationHeaderValue;

        @BeforeEach
        void tokenSetUp() {
            Long authorizedUserId = 1L;
            String token = jwtUtil.encode(authorizedUserId);
            authorizationHeaderValue = "Bearer " + token;
            SessionTokenData sessionTokenData = SessionTokenData.builder()
                    .userId(authorizedUserId)
                    .build();
            given(authenticationService.verify(token))
                    .willReturn(sessionTokenData);
        }

        @Nested
        @DisplayName("올바른 상품 정보를 받으면")
        class ContextWithValidAttributes {

            private ProductData productData;
            private Product product;

            @BeforeEach
            void setUp() {
                productData = ProductData.builder()
                        .name("쥐돌이")
                        .maker("냥이월드")
                        .price(5000)
                        .build();
                product = Product.builder()
                        .id(1L)
                        .name(productData.getName())
                        .maker(productData.getMaker())
                        .price(productData.getPrice())
                        .build();
                given(productService.createProduct(any(ProductData.class)))
                        .willReturn(product);
            }

            @Test
            @DisplayName("Created status와 상품을 반환합니다")
            void ItReturnCreatedWithProduct() throws Exception {
                String requestBody = objectMapper.writeValueAsString(productData);
                String expected = objectMapper.writeValueAsString(product);
                mockMvc.perform(
                        post("/products")
                                .header("Authorization", authorizationHeaderValue)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                        .andExpect(status().isCreated())
                        .andExpect(content().json(expected, true));

                verify(productService).createProduct(any(ProductData.class));
            }
        }

        @Nested
        @DisplayName("올바르지 않은 상품 정보를 받으면")
        class ContextWithInvalidAttributes {

            private ProductData productData;

            @BeforeEach
            void setUp() {
                productData = ProductData.builder()
                        .name("")
                        .maker("")
                        .price(0)
                        .build();
            }

            @Test
            @DisplayName("BadRequest status를 반환합니다")
            void ItReturnsBadRequest() throws Exception {
                String requestBody = objectMapper.writeValueAsString(productData);
                mockMvc.perform(
                        post("/products")
                                .header("Authorization", authorizationHeaderValue)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("Authorization 헤더를 받지 않으면")
        class ContextWithoutAuthorizationHeader {

            private ProductData productData;

            @BeforeEach
            void setUp() {
                productData = ProductData.builder()
                        .name("쥐돌이")
                        .maker("냥이월드")
                        .price(5000)
                        .build();
            }

            @Test
            @DisplayName("BadRequest status를 반환합니다")
            void ItReturnsBadRequest() throws Exception {
                String requestBody = objectMapper.writeValueAsString(productData);
                mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("인가 받지 않은 생성 요청을 받으면")
        class ContextWithUnauthorizedUser {

            private ProductData productData;
            private String unauthorizedHeaderValue;

            @BeforeEach
            void setUp() {
                Long unauthorizedUserId = 100L;
                String unauthorizedToken = jwtUtil.encode(unauthorizedUserId);
                unauthorizedHeaderValue = "Bearer " + unauthorizedToken;
                given(authenticationService.verify(unauthorizedHeaderValue))
                        .willThrow(SignatureException.class);
                productData = ProductData.builder()
                        .name("쥐돌이")
                        .maker("냥이월드")
                        .price(5000)
                        .build();
            }

            @Test
            @DisplayName("Unauthorized status를 반환합니다")
            void ItReturnsUnauthorized() throws Exception {
                String requestBody = objectMapper.writeValueAsString(productData);
                mockMvc.perform(
                        post("/products")
                                .header("Authorization", unauthorizedHeaderValue)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class DescribeUpdate {

        private String authorizationHeaderValue;

        @BeforeEach
        void tokenSetUp() {
            Long authorizedUserId = 1L;
            String token = jwtUtil.encode(authorizedUserId);
            authorizationHeaderValue = "Bearer " + token;
            SessionTokenData sessionTokenData = SessionTokenData.builder()
                    .userId(authorizedUserId)
                    .build();
            given(authenticationService.verify(authorizationHeaderValue))
                    .willReturn(sessionTokenData);
        }

        @Nested
        @DisplayName("존재하는 상품에 대한 ")
        class ContextWithExistedProduct {

            @Nested
            @DisplayName("올바른 정보 수정 요청을 받으면")
            class ContextWithValidData {

                private ProductData productData;
                private Product product;

                @BeforeEach
                void productSetUp() {
                    productData = ProductData.builder()
                            .name("쥐순이")
                            .maker("냥이월드")
                            .price(5000)
                            .build();
                    product = Product.builder()
                            .id(1L)
                            .name(productData.getName())
                            .maker(productData.getMaker())
                            .price(productData.getPrice())
                            .build();
                    given(productService.updateProduct(eq(product.getId()), any(ProductData.class)))
                            .willReturn(product);
                }

                @Test
                @DisplayName("Ok status와 수정된 상품을 반환합니다")
                void ItReturnsOkWithEditedProduct() throws Exception {
                    String requestBody = objectMapper.writeValueAsString(productData);
                    String expected = objectMapper.writeValueAsString(product);
                    mockMvc.perform(
                            patch("/products/" + product.getId())
                                    .header("Authorization", authorizationHeaderValue)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString("쥐순이")));

                    verify(productService).updateProduct(eq(1L), any(ProductData.class));
                }
            }

            @Nested
            @DisplayName("올바르지 않은 정보 수정 요청을 받으면")
            class ContextWithInvalidData {

                private ProductData productData;

                @BeforeEach
                void productDataSetUp() {
                    productData = ProductData.builder()
                            .name("")
                            .maker("")
                            .price(0)
                            .build();
                }

                @Test
                @DisplayName("BadRequest를 반환합니다")
                void ItReturnsBadRequest() throws Exception {
                    String requestBody = objectMapper.writeValueAsString(productData);
                    mockMvc.perform(
                            patch("/products/1")
                                    .header("Authorization", authorizationHeaderValue)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                            .andExpect(status().isBadRequest());
                }
            }
        }

        @Nested
        @DisplayName("존재하지 않는 상품에 대한 수정 요청을 받으면")
        class ContextWithNotExistedProduct {

            private ProductData productData;

            @BeforeEach
            void productSetUp() {
                productData = ProductData.builder()
                        .name("쥐순이")
                        .maker("냥이월드")
                        .price(5000)
                        .build();
                given(productService.updateProduct(eq(1000L), any(ProductData.class)))
                        .willThrow(ProductNotFoundException.class);
            }

            @Test
            @DisplayName("NotFound status를 반환합니다")
            void ItReturnsNotFound() throws Exception {
                String requestBody = objectMapper.writeValueAsString(productData);
                mockMvc.perform(
                        patch("/products/1000")
                                .header("Authorization", authorizationHeaderValue)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                        .andExpect(status().isNotFound());

                verify(productService).updateProduct(eq(1000L), any(ProductData.class));
            }
        }

        @Nested
        @DisplayName("인가 받지 않은 수정 요청을 받으면")
        class ContextWithUnanthorizedRequest {

            private ProductData productData;
            private String unauthorizedHeaderValue;

            @BeforeEach
            void setUp() {
                Long unauthorizedUserId = 1001L;
                String unauthorizedToken = jwtUtil.encode(unauthorizedUserId);
                unauthorizedHeaderValue = "Bearer " + unauthorizedToken;
                given(authenticationService.verify(unauthorizedHeaderValue))
                        .willThrow(SignatureException.class);
                productData = ProductData.builder()
                        .name("쥐돌이")
                        .maker("냥이월드")
                        .price(5000)
                        .build();
            }

            @Test
            @DisplayName("Unauthorized status를 반환합니다")
            void ItReturnsUnanthorized() throws Exception {
                String requestBody = objectMapper.writeValueAsString(productData);
                mockMvc.perform(
                        patch("/products/1")
                                .header("Authorization", unauthorizedHeaderValue)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("destroy 메소드는")
    class DescribeDestroy {

        private String authorizationHeaderValue;

        @BeforeEach
        void tokenSetUp() {
            Long authorizedUserId = 1L;
            String token = jwtUtil.encode(authorizedUserId);
            authorizationHeaderValue = "Bearer " + token;
            SessionTokenData sessionTokenData = SessionTokenData.builder()
                    .userId(authorizedUserId)
                    .build();
            given(authenticationService.verify(authorizationHeaderValue))
                    .willReturn(sessionTokenData);
        }

        @Nested
        @DisplayName("존재하는 상품에 대한 삭제 요청을 받으면")
        class ContextWithExistedProduct {

            @BeforeEach
            void productSetUp() {
                Product product = Product.builder()
                        .id(1L)
                        .name("쥐돌이")
                        .maker("냥이월드")
                        .price(5000)
                        .build();
                given(productService.deleteProduct(1L))
                        .willReturn(product);
            }

            @Test
            @DisplayName("NoContent status를 반환합니다")
            void ItReturnsNoContent() throws Exception {
                mockMvc.perform(
                        delete("/products/1")
                                .header("Authorization", authorizationHeaderValue)
                )
                        .andExpect(status().isNoContent());

                verify(productService).deleteProduct(1L);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 상품에 대한 삭제 요청을 받으면")
        class ContextWithNotExistedProduct {

            @BeforeEach
            void productSetUp() {
                given(productService.deleteProduct(1000L))
                        .willThrow(ProductNotFoundException.class);
            }

            @Test
            @DisplayName("NotFound status를 반환합니다")
            void ItReturnsNotFound() throws Exception {
                mockMvc.perform(
                        delete("/products/1000")
                                .header("Authorization", authorizationHeaderValue)
                )
                        .andExpect(status().isNotFound());

                verify(productService).deleteProduct(1000L);
            }
        }

        @Nested
        @DisplayName("인가 받지 않은 삭제 요청을 받으면")
        class ContextWithUnauthorizedRequest {

            private String unauthorizedHeaderValue;

            @BeforeEach
            void setUp() {
                Long unauthorizedUserId = 1000L;
                String unauthorizedToken = jwtUtil.encode(unauthorizedUserId);
                unauthorizedHeaderValue = "Bearer " + unauthorizedToken;
                given(authenticationService.verify(unauthorizedHeaderValue))
                        .willThrow(SignatureException.class);
            }

            @Test
            @DisplayName("Unauthorized status를 반환합니다")
            void ItReturnsUnauthorized() throws Exception {
                mockMvc.perform(
                        delete("/products/1")
                                .header("Authorization", unauthorizedHeaderValue)
                )
                        .andExpect(status().isUnauthorized());
            }
        }
    }
}
