package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("ProductController 클래스")
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    private final String validToken = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private final String invalidToken = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    private final Long existingId = 1L;
    private final Long notExistingId = 100L;

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

        given(authenticationService.parseToken(validToken))
                .willReturn(1L);
    }

    @AfterEach
    void clear() {
        Mockito.reset(authenticationService);
    }

    @Nested
    @DisplayName("GET 요청은")
    class Describe_GET {
        @Nested
        @DisplayName("저장된 상품이 여러개 있다면")
        class Context_with_products {
            @BeforeEach
            void setUp() {
                products = List.of(product1, product2);

                given(productService.getProducts())
                        .willReturn(products);
            }

            @Test
            @DisplayName("모든 상품 목록과 상태코드 200을 응답한다")
            void it_responds_all_product_list_and_status_code_200() throws Exception {
                mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(products.size())))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("저장된 상품이 없다면")
        class Context_without_products {
            @BeforeEach
            void setUp() {
                given(productService.getProducts())
                        .willReturn(List.of());
            }

            @Test
            @DisplayName("비어있는 상품 목록과 상태코드 200을 응답한다")
            void it_responds_empty_product_list_and_status_code_200() throws Exception {
                mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(0)))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하는 상품 id가 주어진다면")
        class Context_with_an_existing_product_id {
            @BeforeEach
            void setUp() {
                given(productService.getProduct(existingId))
                        .willReturn(product1);
            }

            @Test
            @DisplayName("찾은 상품과 상태코드 200을 응답한다")
            void it_responds_the_found_product_and_status_code_200() throws Exception {
                mockMvc.perform(get("/products/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("name").exists())
                        .andExpect(jsonPath("maker").exists())
                        .andExpect(jsonPath("price").exists())
                        .andExpect(jsonPath("imageUrl").exists())
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 상품 id가 주어진다면")
        class Context_with_not_existing_product_id {
            @BeforeEach
            void setUp() {
                given(productService.getProduct(notExistingId))
                        .willThrow(new ProductNotFoundException(notExistingId));
            }

            @Test
            @DisplayName("에러메시지와 상태코드 404를 응답한다")
            void it_responds_the_error_message_and_status_code_404() throws Exception {
                mockMvc.perform(get("/products/{id}", notExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("name").doesNotExist())
                        .andExpect(jsonPath("message").exists())
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("POST 요청은")
    class Describe_POST {
        @Nested
        @DisplayName("올바른 상품 정보가 주어진다면")
        class Context_with_a_valid_product {
            @BeforeEach
            void setUp() {
                given(productService.createProduct(any(ProductData.class)))
                        .willReturn(product1);
            }

            @Test
            @DisplayName("생성된 상품과 상태코드 201을 응답한다.")
            void it_responds_the_created_product_and_status_code_201() throws Exception {
                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + validToken)
                        .content(objectMapper.writeValueAsString(validProductData)))
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("name").exists())
                        .andExpect(jsonPath("maker").exists())
                        .andExpect(jsonPath("price").exists())
                        .andExpect(jsonPath("imageUrl").exists())
                        .andExpect(status().isCreated());
            }
        }

        @Nested
        @DisplayName("올바르지 않은 상품 정보가 주어진다면")
        class Context_with_an_invalid_product {
            @Test
            @DisplayName("상태코드 400을 응답한다.")
            void it_responds_status_code_400() throws Exception {
                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + validToken)
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
                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductData)))
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("올바르지 않은 액세스 토큰이 주어진다면")
        class Context_with_invalid_access_token {
            @BeforeEach
            void setup() {
                given(authenticationService.parseToken(invalidToken))
                        .willThrow(new InvalidTokenException(invalidToken));
            }

            @Test
            @DisplayName("상태코드 401을 응답한다.")
            void it_responds_status_code_401() throws Exception {
                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + invalidToken)
                        .content(objectMapper.writeValueAsString(validProductData)))
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("PATCH 요청은")
    class Describe_PATCH {
        @Nested
        @DisplayName("존재하는 상품 id가 주어진다면")
        class Context_with_an_existing_product_id {
            @BeforeEach
            void setUp() {
                given(productService.updateProduct(eq(existingId), any(ProductData.class)))
                        .willReturn(product1);
            }

            @Test
            @DisplayName("수정된 상품과 상태코드 200을 응답한다.")
            void it_responds_the_updated_product_and_status_code_200() throws Exception {
                mockMvc.perform(patch("/products/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + validToken)
                        .content(objectMapper.writeValueAsString(validProductData)))
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("name").exists())
                        .andExpect(jsonPath("maker").exists())
                        .andExpect(jsonPath("price").exists())
                        .andExpect(jsonPath("imageUrl").exists())
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 상품 id가 주어진다면")
        class Context_with_not_existing_product_id {
            @BeforeEach
            void setUp() {
                given(productService.updateProduct(eq(notExistingId), any(ProductData.class)))
                        .willThrow(new ProductNotFoundException(notExistingId));
            }

            @Test
            @DisplayName("에러메시지와 상태코드 404를 응답한다.")
            void it_responds_the_error_message_and_status_code_404() throws Exception {
                mockMvc.perform(patch("/products/{id}", notExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + validToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductData)))
                        .andExpect(jsonPath("name").doesNotExist())
                        .andExpect(jsonPath("message").exists())
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("올바르지 않은 상품 정보가 주어진다면")
        class Context_with_an_invalid_product {
            @Test
            @DisplayName("상태코드 400을 응답한다.")
            void it_responds_status_code_400() throws Exception {
                mockMvc.perform(patch("/products/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + validToken)
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
                mockMvc.perform(patch("/products/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductData)))
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("올바르지 않은 액세스 토큰이 주어진다면")
        class Context_with_invalid_access_token {
            @BeforeEach
            void setup() {
                given(authenticationService.parseToken(invalidToken))
                        .willThrow(new InvalidTokenException(invalidToken));
            }

            @Test
            @DisplayName("상태코드 401을 응답한다.")
            void it_responds_status_code_401() throws Exception {
                mockMvc.perform(patch("/products/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + invalidToken)
                        .content(objectMapper.writeValueAsString(validProductData)))
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("DELETE 요청은")
    class Describe_DELETE {
        @Nested
        @DisplayName("존재하는 상품 id가 주어진다면")
        class Context_with_an_existing_product_id {
            @Test
            @DisplayName("상태코드 204을 응답한다")
            void it_responds_status_code_204() throws Exception {
                mockMvc.perform(delete("/products/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + validToken))
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 상품 id가 주어진다면")
        class Context_with_not_existing_product_id {
            @BeforeEach
            void setUp() {
                given(productService.deleteProduct(notExistingId))
                        .willThrow(new ProductNotFoundException(notExistingId));
            }

            @Test
            @DisplayName("에러메시지와 상태코드 404를 응답한다.")
            void it_responds_the_error_message_and_status_code_404() throws Exception {
                mockMvc.perform(delete("/products/{id}", notExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + validToken))
                        .andExpect(jsonPath("name").doesNotExist())
                        .andExpect(jsonPath("message").exists())
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("액세스 토큰이 주어지지 않는다면")
        class Context_without_access_token {
            @Test
            @DisplayName("상태코드 401을 응답한다.")
            void it_responds_status_code_401() throws Exception {
                mockMvc.perform(delete("/products/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("올바르지 않은 액세스 토큰이 주어진다면")
        class Context_with_invalid_access_token {
            @BeforeEach
            void setup() {
                given(authenticationService.parseToken(invalidToken))
                        .willThrow(new InvalidTokenException(invalidToken));
            }

            @Test
            @DisplayName("상태코드 401을 응답한다.")
            void it_responds_status_code_401() throws Exception {
                mockMvc.perform(delete("/products/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + invalidToken))
                        .andExpect(status().isUnauthorized());
            }
        }
    }

}
