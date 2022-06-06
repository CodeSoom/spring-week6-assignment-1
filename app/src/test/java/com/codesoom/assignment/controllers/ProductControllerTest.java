package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.BadRequestException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";
    private static final Long ID = 1L;
    private static final Long NON_EXISTING_ID = 1000L;
    private static final String NAME = "쥐돌이";
    private static final String INVALID_NAME = "";
    private static final String NEW_NAME = "쥐순이";
    private static final String MAKER = "냥이월드";
    private static final int PRICE = 5000;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Product product;
    private ProductData productData;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(ID)
                .name(NAME)
                .maker(MAKER)
                .price(PRICE)
                .build();

        productData = ProductData.builder()
                .name(NAME)
                .maker(MAKER)
                .price(PRICE)
                .build();
    }

    @Nested
    @DisplayName("GET /products 요청 시")
    class Describe_get_products {
        @Nested
        @DisplayName("1개의 product가 저장되어 있는 경우")
        class Context_if_one_product_stored {
            @BeforeEach
            void setUp() {
                given(productService.getProducts()).willReturn(List.of(product));
            }

            @Nested
            @DisplayName("status code 200을 응답한다")
            class It_response_status_code_200{
                ResultActions subject() throws Exception {
                    return mockMvc.perform(get("/products"));
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isOk())
                            .andExpect(jsonPath("$[0].id").value(ID))
                            .andExpect(jsonPath("$[0].name").value(NAME))
                            .andExpect(jsonPath("$[0].maker").value(MAKER))
                            .andExpect(jsonPath("$[0].price").value(PRICE));
                }
            }
        }
    }

    @Nested
    @DisplayName("GET /products/{id} 요청 시")
    class Describe_get_products_by_id {
        @Nested
        @DisplayName("존재하는 id가 주어진 경우")
        class Context_if_existing_id_given {
            @BeforeEach
            void setUp() {
                given(productService.getProduct(ID)).willReturn(product);
            }

            @Nested
            @DisplayName("status code 200을 응답한다")
            class It_response_status_code_200{
                ResultActions subject() throws Exception {
                    return mockMvc.perform(get("/products/{id}", ID));
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isOk())
                            .andExpect(jsonPath("$.id").value(ID))
                            .andExpect(jsonPath("$.name").value(NAME))
                            .andExpect(jsonPath("$.maker").value(MAKER))
                            .andExpect(jsonPath("$.price").value(PRICE));
                }
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어진 경우")
        class Context_if_non_existing_id_given {
            @BeforeEach
            void setUp() {
                given(productService.getProduct(NON_EXISTING_ID))
                        .willThrow(new ProductNotFoundException(NON_EXISTING_ID));
            }

            @Nested
            @DisplayName("status code 404를 응답한다")
            class It_response_status_code_404 {
                ResultActions subject() throws Exception {
                    return mockMvc.perform(get("/products/{id}", NON_EXISTING_ID));
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isNotFound());
                }
            }
        }
    }

    @Nested
    @DisplayName("POST /products 요청 시")
    class Describe_post_products {
        @Nested
        @DisplayName("모든 속성이 유효한 product가 주어졌을 경우")
        class Context_if_product_with_valid_attributes_given {
            @BeforeEach
            void setUp() {
                given(productService.createProduct(any(ProductData.class))).willReturn(product);
            }

            @Nested
            @DisplayName("status code 201을 응답한다")
            class It_response_status_code_201 {
                ResultActions subject() throws Exception {
                    return mockMvc.perform(post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(productData))
                    );
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isCreated())
                            .andExpect(jsonPath("$.name").value(NAME))
                            .andExpect(jsonPath("$.maker").value(MAKER))
                            .andExpect(jsonPath("$.price").value(PRICE));

                    verify(productService).createProduct(any(ProductData.class));
                }
            }
        }

        @Nested
        @DisplayName("유효하지 않은 이름을 가진 product가 주어졌을 경우")
        class Context_if_product_with_invalid_name_given {
            @BeforeEach
            void setUp() {
                productData = ProductData.builder()
                        .name(INVALID_NAME)
                        .maker(MAKER)
                        .price(PRICE)
                        .build();

                given(productService.createProduct(eq(productData)))
                        .willThrow(new BadRequestException());
            }

            @Nested
            @DisplayName("status code 400을 응답한다")
            class It_response_status_code_400 {
                ResultActions subject() throws Exception {
                    return mockMvc.perform(post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(productData))
                    );
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isBadRequest());
                }
            }
        }
    }

    @Nested
    @DisplayName("PATCH /products/{id} 요청 시")
    class Describe_patch_products {
        @BeforeEach
        void setUp() {
            productData = ProductData.builder()
                    .name(NEW_NAME)
                    .maker(MAKER)
                    .price(PRICE)
                    .build();
        }

        @Nested
        @DisplayName("존재하는 id가 주어졌을 경우")
        class Context_if_existing_id_given {
            @BeforeEach
            void setUp() {
                given(productService.updateProduct(eq(ID), any(ProductData.class)))
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

            @Nested
            @DisplayName("status code 200을 응답한다")
            class It_response_status_code_200{
                ResultActions subject() throws Exception {
                    return mockMvc.perform(patch("/products/{id}", ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(productData))
                    );
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isOk())
                            .andExpect(jsonPath("$.name").value(NEW_NAME));
                }
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어졌을 경우")
        class Context_if_non_existing_id_given {
            @BeforeEach
            void setUp() {
                given(productService.updateProduct(eq(NON_EXISTING_ID), any(ProductData.class)))
                        .willThrow(new ProductNotFoundException(NON_EXISTING_ID));
            }

            @Nested
            @DisplayName("status code 404를 응답한다")
            class It_response_status_code_404 {
                ResultActions subject() throws Exception {
                    return mockMvc.perform(patch("/products/{id}", NON_EXISTING_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(productData))
                    );
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isNotFound());
                }
            }
        }

        @Nested
        @DisplayName("유효하지 않은 이름을 가진 product가 주어졌을 경우")
        class Context_if_product_with_invalid_name_given {
            @BeforeEach
            void setUp() {
                productData = ProductData.builder()
                        .name(INVALID_NAME)
                        .maker(MAKER)
                        .price(PRICE)
                        .build();

                given(productService.updateProduct(eq(ID), eq(productData)))
                        .willThrow(new BadRequestException());
            }

            @Nested
            @DisplayName("status code 400을 응답한다")
            class It_response_status_code_400 {
                ResultActions subject() throws Exception {
                    return mockMvc.perform(patch("/products/{id}", ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(productData))
                    );
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isBadRequest());
                }
            }
        }
    }

    @Nested
    @DisplayName("DELETE /products/{id} 요청 시")
    class Describe_delete_products {
        @Nested
        @DisplayName("존재하는 id가 주어졌을 경우")
        class Context_if_existing_id_given {
            @Nested
            @DisplayName("status code 204를 응답한다")
            class It_response_status_code_204{
                ResultActions subject() throws Exception {
                    return mockMvc.perform(delete("/products/{id}", ID));
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isNoContent());

                    verify(productService).deleteProduct(ID);
                }
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어졌을 경우")
        class Context_if_non_existing_id_given {
            @BeforeEach
            void setUp() {
                given(productService.deleteProduct(NON_EXISTING_ID))
                        .willThrow(new ProductNotFoundException(NON_EXISTING_ID));
            }

            @Nested
            @DisplayName("status code 404를 응답한다")
            class It_response_status_code_404 {
                ResultActions subject() throws Exception {
                    return mockMvc.perform(delete("/products/{id}", NON_EXISTING_ID));
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isNotFound());
                }
            }
        }
    }

    private String toJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(object);
    }
}
