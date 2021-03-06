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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";
    private final long ID = 1L;
    private final long NOT_EXIST_ID = 100L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    private List<Product> productList;
    private Product validProduct;
    private ProductData validProductData;

    @BeforeEach
    void setUp() {
        validProduct = Product.builder()
                .id(ID)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .build();

        validProductData = ProductData.builder()
                .id(validProduct.getId())
                .name(validProduct.getName())
                .maker(validProduct.getMaker())
                .price(validProduct.getPrice())
                .build();

        given(authenticationService.parseToken(INVALID_TOKEN))
                .willThrow(new InvalidTokenException(INVALID_TOKEN));
    }

    @AfterEach
    void reset() {
        Mockito.reset(authenticationService);
    }

    @Nested
    @DisplayName("Get /products 요청")
    class Describe_getProducts {
        @Nested
        @DisplayName("서비스에 상품이 1개 이상 있으면")
        class Context_exist_product {
            @BeforeEach
            void setUp() {
                productList = new ArrayList<>();
                productList.add(validProduct);
                given(productService.getProducts()).willReturn(productList);
            }

            @Test
            @DisplayName("상품 list를 응답한다")
            void it_return_empty_list() throws Exception {
                mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(productList.size())));
            }
        }

        @Nested
        @DisplayName("저장된 상품이 없으면")
        class Context_does_not_exist_product {
            @Test
            @DisplayName("빈 list를 응답한다")
            void it_return_empty_list() throws Exception {
                mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").doesNotExist());
            }
        }
    }

    @Nested
    @DisplayName("Get /products/{id} 요청")
    class Describe_getProduct {
        @Nested
        @DisplayName("id가 존재하는 상품이면")
        class Context_exist_id {
            @BeforeEach
            void setUp() {
                given(productService.getProduct(ID)).willReturn(validProduct);
            }

            @Test
            @DisplayName("해당하는 상품을 응답한다")
            void it_return_empty_list() throws Exception {
                mockMvc.perform(get("/products/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("name").exists());
            }
        }

        @Nested
        @DisplayName("주어진 id로 찾을수 없는 상품이면")
        class Context_does_not_exist_id {
            @BeforeEach
            void setUp() {
                given(productService.getProduct(NOT_EXIST_ID))
                        .willThrow(new ProductNotFoundException(NOT_EXIST_ID));
            }

            @Test
            @DisplayName("응답코드는 404이며 id가 존재하지 않는다는 메세지를 응답한다.")
            void it_return_not_found() throws Exception {
                mockMvc.perform(get("/products/{id}", NOT_EXIST_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("message").value("Product not found"));
            }
        }
    }

    @Nested
    @DisplayName("Post /products 요청은")
    class Describe_addProduct {
        @Nested
        @DisplayName("상품이 있으면")
        class Context_exist_product {
            @BeforeEach
            void setUp() {
                given(productService.createProduct(any(ProductData.class))).willReturn(validProduct);
            }

            @Test
            @DisplayName("응답코드 201와 저장된 상품을 응답한다")
            void it_return_product() throws Exception {
                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(validProductData))
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("name").exists())
                        .andExpect(jsonPath("maker").exists());
            }
        }

        @Nested
        @DisplayName("상품이 없으면")
        class Context_does_not_exist_product {
            @Test
            @DisplayName("bad request를 응답한다")
            void it_return_bad_request() throws Exception {
                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("엑세스토큰이 없으면")
        class Context_without_access_token {

            @Test
            @DisplayName("401 unauthorized로 응답한다")
            void it_response_unauthorized() throws Exception {
                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(validProductData)))
                        .andDo(print())
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("엑세스토큰이 올바르지 않다면")
        class Context_without_invalid_access_token {

            @Test
            @DisplayName("401 unauthorized로 응답한다")
            void it_response_unauthorized() throws Exception {
                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
                        .content(objectMapper.writeValueAsString(validProductData)))
                        .andDo(print())
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("Patch /products/{id} 요청은")
    class Describe_patchProduct {
        Product changeProduct;
        ProductData changeProductData;

        @BeforeEach
        void setUp() {
            changeProduct = new Product(ID, "바뀐 장난감", "다른 회사", 500, "바뀐이미지.jpg");
            changeProductData = ProductData.builder()
                    .id(changeProduct.getId())
                    .name(changeProduct.getName())
                    .maker(changeProduct.getMaker())
                    .price(changeProduct.getPrice())
                    .imageUrl(changeProduct.getImageUrl())
                    .build();
        }

        @Nested
        @DisplayName("id와 ProductData가 있으면")
        class Context_exist_id_and_productDto {
            @BeforeEach
            void setUp() {
                given(productService.updateProduct(eq(ID), any(ProductData.class))).willReturn(changeProduct);
            }

            @Test
            @DisplayName("응답코드 200과 변경된 상품을 응답한다.")
            void it_return_changed_product() throws Exception {
                mockMvc.perform(patch("/products/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .content(objectMapper.writeValueAsString(changeProductData)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("name").value("바뀐 장난감"));
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않으면")
        class Context_does_not_exist_id {
            @BeforeEach
            void setUp() {
                given(productService.updateProduct(eq(NOT_EXIST_ID), any(ProductData.class)))
                        .willThrow(new ProductNotFoundException(NOT_EXIST_ID));
            }

            @Test
            @DisplayName("응답코드는 404이며 에러메세지를 응답한다.")
            void it_return_not_foud() throws Exception {
                mockMvc.perform(patch("/products/{id}", NOT_EXIST_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .content(objectMapper.writeValueAsString(changeProductData)))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("message").exists());
            }
        }

        @Nested
        @DisplayName("수정할 상품정보가 올바르지 않으면")
        class Context_with_invalid_product_data {
            ProductData invalidProductData;

            @BeforeEach
            void setUp() {
                invalidProductData = ProductData.builder()
                        .name("")
                        .maker("")
                        .price(0)
                        .imageUrl("")
                        .build();
            }

            @Test
            @DisplayName("400 bad request로 응답한다")
            void it_throw_bad_request() throws Exception {
                mockMvc.perform(patch("/products/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .content(objectMapper.writeValueAsString(invalidProductData)))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("액세스 토큰이 없다면")
        class Context_without_access_token {

            @Test
            @DisplayName("401 unauthorized로 응답한다")
            void it_response_unauthorized() throws Exception {
                mockMvc.perform(patch("/products/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(changeProductData)))
                        .andDo(print())
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("액세스 토큰이 올바르지 않다면")
        class Context_with_invalid_access_token {

            @Test
            @DisplayName("401 unauthorized로 응답한다")
            void it_response_unauthorized() throws Exception {
                mockMvc.perform(patch("/products/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
                        .content(objectMapper.writeValueAsString(validProductData)))
                        .andDo(print())
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("Delete /products/{id} 요청")
    class Describe_deleteProduct {
        @Nested
        @DisplayName("id가 존재하는 상품이면")
        class Context_exist_id {
            @Test
            @DisplayName("204 no content로 응답한다.")
            void it_return_no_content() throws Exception {
                mockMvc.perform(delete("/products/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                        .andDo(print())
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않는 상품이면")
        class Context_does_not_exist_id {
            @BeforeEach
            void setUp() {
                willThrow(new ProductNotFoundException(NOT_EXIST_ID)).given(productService).deleteProduct(NOT_EXIST_ID);
            }

            @Test
            @DisplayName("응답코드는 404이며 id가 존재하지 않는다는 메세지를 응답한다.")
            void it_return_not_found() throws Exception {
                mockMvc.perform(delete("/products/{id}", NOT_EXIST_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("message").value("Product not found"));
            }
        }

        @Nested
        @DisplayName("액세스 토큰이 없다면")
        class Context_without_access_token {

            @Test
            @DisplayName("401 unauthorized로 응답한다")
            void it_response_unauthorized() throws Exception {
                mockMvc.perform(delete("/products/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("액세스 토큰이 올바르지 않다면")
        class Context_with_invalid_access_token {

            @Test
            @DisplayName("401 unauthorized로 응답한다")
            void it_response_unauthorized() throws Exception {
                mockMvc.perform(delete("/products/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + INVALID_TOKEN))
                        .andDo(print())
                        .andExpect(status().isUnauthorized());
            }
        }
    }
}
