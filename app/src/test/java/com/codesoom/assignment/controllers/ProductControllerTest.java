package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("ProductController 클래스")
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private ProductService productService;

    private final Long EXISTING_ID = 1L;
    private final Long NOT_EXISTING_ID = 100L;

    private List<Product> products;
    private Product product1;
    private Product product2;
    private ProductData validProductData;
    private ProductData invalidProductData;

    @BeforeEach
    void setUp() {
        product1 = Product.builder()
                .id(1L)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .imageUrl("url1")
                .build();

        product2 = Product.builder()
                .id(2L)
                .name("쥐순이")
                .maker("쥐순이월드")
                .price(10000)
                .imageUrl("url2")
                .build();

        validProductData = ProductData.builder()
                .name("새로운 쥐돌이")
                .maker("새로운 냥이월드")
                .price(20000)
                .imageUrl("새로운 url")
                .build();

        invalidProductData = ProductData.builder()
                .name("")
                .maker("")
                .price(0)
                .imageUrl("")
                .build();

        given(authenticationService.parseToken(VALID_TOKEN))
                .willReturn(EXISTING_ID);
    }

    @AfterEach
    void clear() {
        Mockito.reset(authenticationService);
    }

    @Nested
    @DisplayName("GET 요청은")
    class Describe_GET {

        @Nested
        @DisplayName("저장된 상품이 여러 개라면")
        class Context_with_products {

            @BeforeEach
            void setUp() {
                products = List.of(product1, product2);

                given(productService.getProducts()).willReturn(products);
            }

            @Test
            @DisplayName("모든 상품 목록과 상태코드 200을 응답한다.")
            void it_return_products_and_status_code_200() throws Exception {
                mockMvc.perform(get("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(products.size())))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("저장된 상품 id가 주어지면")
        class Context_with_a_stored_product_id {

            @BeforeEach
            void setUp() {
                given(productService.getProduct(EXISTING_ID)).willReturn(product1);
            }

            @Test
            @DisplayName("찾은 상품과 상태코드 200을 응답한다.")
            void it_responds_the_product_and_status_code_200() throws Exception {
                mockMvc.perform(get("/products/{id}", EXISTING_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("name").exists())
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("저장되지 않은 상품 id가 주어지면")
        class Context_with_a_not_stored_product_id {

            @BeforeEach
            void setUp() {
                given(productService.getProduct(NOT_EXISTING_ID))
                        .willThrow(new ProductNotFoundException(NOT_EXISTING_ID));
            }

            @Test
            @DisplayName("에러메시지와 상태코드 404을 응답한다.")
            void it_responds_the_product_and_status_code_200() throws Exception {
                mockMvc.perform(get("/products/{id}", NOT_EXISTING_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("id").doesNotExist())
                        .andExpect(jsonPath("message").exists())
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("POST 요청은")
    class Describe_POST {

        @Nested
        @DisplayName("유효한 상품 정보가 주어지면")
        class Context_with_a_product {

            @BeforeEach
            void setUp() {
                given(productService.createProduct(any(ProductData.class)))
                        .willReturn(product1);
            }

            @Test
            @DisplayName("생성한 상품과 상태코드 201을 응답한다.")
            void it_responds_the_created_product_and_status_code_201() throws Exception {
                mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                                .content(objectMapper.writeValueAsString(validProductData)))
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("name").exists())
                        .andExpect(status().isCreated());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 상품 정보가 주어지면")
        class Context_with_an_invalid_product {

            @Test
            @DisplayName("상태코드 400을 응답한다.")
            void it_responds_status_code_400() throws Exception {
                mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                                .content(objectMapper.writeValueAsString(invalidProductData)))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("액세스 토큰이 주어지지 않는다면")
        class Context_without_access_token {

            @Test
            @DisplayName("상태코드 401을 응답한다.")
            void it_responds_status_code_400() throws Exception {
                mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validProductData)))
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 액세스 토큰이 주어지면")
        class Context_with_invalid_access_token {

            @BeforeEach
            void setUp() {
                given(authenticationService.parseToken(INVALID_TOKEN))
                        .willThrow(new InvalidTokenException(INVALID_TOKEN));
            }

            @Test
            @DisplayName("상태코드 401을 응답한다.")
            void it_responds_status_code_401() throws Exception {
                mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + INVALID_TOKEN)
                                .content(objectMapper.writeValueAsString(validProductData)))
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("PATCH 요청은")
    class Describe_PATCH {

        @Nested
        @DisplayName("저장된 상품 id가 주어지면")
        class Context_with_stored_product_id {

            @BeforeEach
            void setUp() {
                given(productService.updateProduct(eq(EXISTING_ID), any(ProductData.class)))
                        .willReturn(product1);
            }

            @Test
            @DisplayName("수정한 상품과 상태코드 200을 응답한다.")
            void it_responds_the_updated_product_and_status_code_200() throws Exception {
                mockMvc.perform(patch("/products/{id}", EXISTING_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer" + VALID_TOKEN)
                                .content(objectMapper.writeValueAsString(validProductData)))
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("name").exists())
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("저장되지 않은 상품 id가 주어지면")
        class Context_with_not_stored_product_id {

            @BeforeEach
            void setUp() {
                given(productService.updateProduct(eq(NOT_EXISTING_ID), any(ProductData.class)))
                        .willThrow(new ProductNotFoundException(NOT_EXISTING_ID));
            }

            @Test
            @DisplayName("에러메시지와 상태코드 404를 응답한다.")
            void it_responds_status_code_404() throws Exception {
                mockMvc.perform(patch("/products/{id}", NOT_EXISTING_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer" + VALID_TOKEN)
                                .content(objectMapper.writeValueAsString(validProductData)))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("유효하지 않은 상품 정보가 주어진다면")
    class Context_with_an_invalid_product {

        @Test
        @DisplayName("상태코드 400을 응답한다.")
        void it_responds_status_code_400() throws Exception {
            mockMvc.perform(patch("/products/{id}", EXISTING_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + VALID_TOKEN)
                            .content(objectMapper.writeValueAsString(invalidProductData)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("액세스 토큰이 주어지지 않는다면")
    class Context_without_access_token {

        @Test
        @DisplayName("상태코드 401을 응답한다.")
        void it_responds_status_code_401() throws Exception {
            mockMvc.perform(patch("/products/{id}", EXISTING_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validProductData)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("유효하지 않은 액세스 토큰이 주어지면")
    class Context_with_invalid_access_token {
        @BeforeEach
        void setup() {
            given(authenticationService.parseToken(INVALID_TOKEN))
                    .willThrow(new InvalidTokenException(INVALID_TOKEN));
        }

        @Test
        @DisplayName("상태코드 401을 응답한다.")
        void it_responds_status_code_401() throws Exception {
            mockMvc.perform(patch("/products/{id}", EXISTING_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + INVALID_TOKEN)
                            .content(objectMapper.writeValueAsString(validProductData)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("DELETE 요청은")
    class Describe_DELETE {

        @Nested
        @DisplayName("저장된 상품 id가 주어지면")
        class Context_with_stored_product_id {

            @Test
            @DisplayName("상태코드 204를 응답한다.")
            void it_responds_status_code_204() throws Exception {
                mockMvc.perform(delete("/products/{id}", EXISTING_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + VALID_TOKEN))
                        .andExpect(jsonPath("id").doesNotExist())
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("저장되지 않은 상품 id가 주어지면")
        class Context_with_not_stored_product_id {

            @BeforeEach
            void setUp() {
                given(productService.deleteProduct(NOT_EXISTING_ID))
                        .willThrow(new ProductNotFoundException(NOT_EXISTING_ID));
            }

            @Test
            @DisplayName("상태코드 404를 응답한다.")
            void it_responds_status_code_404() throws Exception {
                mockMvc.perform(delete("/products/{id}", NOT_EXISTING_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + VALID_TOKEN))
                        .andExpect(jsonPath("id").doesNotExist())
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("액세스 토큰이 주어지지 않으면")
        class Context_without_access_token {

            @Test
            @DisplayName("상태코드 401을 응답한다.")
            void it_responds_status_code_401() throws Exception {
                mockMvc.perform(delete("/products/{id}", EXISTING_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 액세스 토큰이 주어지면")
        class Context_with_invalid_access_token {

            @BeforeEach
            void setUp() {
                given(authenticationService.parseToken(INVALID_TOKEN))
                        .willThrow(new InvalidTokenException(INVALID_TOKEN));
            }

            @Test
            @DisplayName("상태코드 401을 응답한다.")
            void it_responds_status_code_401() throws Exception {
                mockMvc.perform(delete("/products/{id}", EXISTING_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + INVALID_TOKEN))
                        .andExpect(status().isUnauthorized());
            }
        }
    }
}