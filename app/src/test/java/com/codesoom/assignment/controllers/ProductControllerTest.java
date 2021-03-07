package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import java.util.List;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ProductController 클래스")
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";
    private static final Long EXIST_ID = 1L;
    private static final Long NOT_EXIST_ID = 1000L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        Mockito.reset(productService);
        Mockito.reset(authenticationService);
        Product product = Product.builder()
                .id(1L)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .build();
        given(productService.getProducts()).willReturn(List.of(product));

        given(productService.getProduct(1L)).willReturn(product);

        given(productService.getProduct(1000L))
                .willThrow(new ProductNotFoundException(1000L));

        given(productService.createProduct(any(ProductData.class)))
                .willReturn(product);

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

        given(productService.updateProduct(eq(1000L), any(ProductData.class)))
                .willThrow(new ProductNotFoundException(1000L));

        given(productService.deleteProduct(1000L))
                .willThrow(new ProductNotFoundException(1000L));

        given(authenticationService.decode(INVALID_TOKEN))
                .willThrow(new InvalidAccessTokenException(INVALID_TOKEN));
    }

    @DisplayName("OPTIONS /products")
    @Nested
    class Describe_OPTIONS_Products {
        @DisplayName("OK(200 코드)를 응답한다")
        @Test
        void it_returns_OK_200_code() throws Exception {
            mockMvc.perform(
                options("/products")
            )
                .andExpect(status().isOk());
        }
    }

    @DisplayName("GET /products")
    @Nested
    class Describe_GET_Products {
        @DisplayName("존재하는 모든 product 목록을 응답한다")
        @Test
        void it_returns_products_lists() throws Exception {
            mockMvc.perform(
                    get("/products")
                            .accept(MediaType.APPLICATION_JSON_UTF8)
            )
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("쥐돌이")));
        }
    }

    @DisplayName("GET /products/{id}")
    @Nested
    class Describe_GET_Products_id {
        @DisplayName("존재하는 product id가 주어진다면")
        @Nested
        class Context_with_exist_product_id {
            Long givenId = EXIST_ID;

            @DisplayName("200 코드와 주어진 id와 일치하는 product를 응답한다")
            @Test
            void it_returns_200_code_with_product() throws Exception {
                mockMvc.perform(
                        get("/products/{id}", givenId)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("쥐돌이")));
            }
        }

        @DisplayName("존재하지 않는 product id가 주어진다면")
        @Nested
        class Context_with_not_exist_product_id {
            Long givenId = NOT_EXIST_ID;

            @DisplayName("404코드를 응답한다")
            @Test
            void it_returns_404_code() throws Exception {
                mockMvc.perform(
                        get("/products/{id}", givenId)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                        .andExpect(status().isNotFound());
            }
        }
    }


    @DisplayName("POST /products")
    @Nested
    class Describe_POST_products {
        @DisplayName("유효한 access token과")
        @Nested
        class Context_with_valid_token {
            String givenToken = "Bearer " + VALID_TOKEN;

            @DisplayName("유효한 product 정보들이 주어졌을 때")
            @Nested
            class Context_with_valid_product {
                @DisplayName("201 코드와 생성된 product를 응답한다")
                @Test
                void it_returns_201_code_with_product() throws Exception {
                    mockMvc.perform(
                            post("/products")
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .header("Authorization", givenToken)
                                    .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                            "\"price\":5000}")
                    )
                            .andExpect(status().isCreated())
                            .andExpect(content().string(containsString("쥐돌이")));

                    verify(productService).createProduct(any(ProductData.class));
                }
            }

            @DisplayName("유효하지 않은 product 정보들이 주어졌을 때")
            @Nested
            class Context_with_invalid_product {
                @DisplayName("400 코드를 응답한다")
                @Test
                void it_returns_400_code() throws Exception {
                    mockMvc.perform(
                            post("/products")
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .header("Authorization", givenToken)
                                    .content("{\"name\":\"\",\"maker\":\"\"," +
                                            "\"price\":0}")
                    )
                            .andExpect(status().isBadRequest());
                }
            }
        }

        @DisplayName("유효하지 않은 access token과")
        @Nested
        class Context_with_invalid_token {
            String givenToken = "Bearer " + INVALID_TOKEN;

            @DisplayName("유효한 product 정보들이 주어졌을 때")
            @Nested
            class Context_with_valid_product {
                @DisplayName("401 코드를 응답한다")
                @Test
                void it_returns_401_code() throws Exception {
                    mockMvc.perform(
                            post("/products")
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .header("Authorization", givenToken)
                                    .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                            "\"price\":5000}")
                    )
                            .andExpect(status().isUnauthorized());
                }
            }
        }

        @DisplayName("비어 있는 access token이 주어졌을 때")
        @Nested
        class Context_with_empty_access_token {

            @DisplayName("Unauthorized(401코드)를 응답한다")
            @Test
            void it_returns_401_code() throws Exception {
                mockMvc.perform(
                    post("/products")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                            "\"price\":5000}")
                )
                    .andExpect(status().isUnauthorized());
            }
        }
    }

    @DisplayName("PATCH /products/{id}")
    @Nested
    class Describe_PATCH_prodcuts_id {
        @DisplayName("유효한 access token과")
        @Nested
        class Context_with_valid_token {
            String givenToken = "Bearer " + VALID_TOKEN;

            @DisplayName("존재하는 product id가 주어진다면")
            @Nested
            class Context_with_exist_product_id {
                Long givenId = EXIST_ID;

                @DisplayName("200 코드와 주어진 수정된 product를 응답한다")
                @Test
                void it_returns_200_code_with_product() throws Exception {
                    mockMvc.perform(
                            patch("/products/{id}", givenId)
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .header("Authorization", givenToken)
                                    .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                            "\"price\":5000}")
                    )
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString("쥐순이")));

                    verify(productService).updateProduct(eq(givenId), any(ProductData.class));
                }
            }

            @DisplayName("존재하지 않는 product id가 주어진다면")
            @Nested
            class Context_with_not_exist_product_id {
                Long givenId = NOT_EXIST_ID;

                @DisplayName("404코드를 응답한다")
                @Test
                void it_returns_404_code_with_product() throws Exception {
                    mockMvc.perform(
                            patch("/products/{id}", givenId)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .header("Authorization", givenToken)
                                    .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                            "\"price\":5000}")
                    )
                            .andExpect(status().isNotFound());

                    verify(productService).updateProduct(eq(givenId), any(ProductData.class));
                }
            }

            @DisplayName("유효하지 않은 product 정보들이 주어졌을 때")
            @Nested
            class Context_with_invalid_product {
                Long givenId = EXIST_ID;

                @DisplayName("400 코드를 응답한다")
                @Test
                void it_returns_400_code() throws Exception {
                    mockMvc.perform(
                            patch("/products/{id}", givenId)
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .header("Authorization", givenToken)
                                    .content("{\"name\":\"\",\"maker\":\"\"," +
                                            "\"price\":0}")
                    )
                            .andExpect(status().isBadRequest());
                }
            }
        }

        @DisplayName("유효하지 않은 access token과")
        @Nested
        class Context_with_invalid_token {
            String givenToken = "Bearer " + INVALID_TOKEN;

            @DisplayName("존재하는 product id가 주어진다면")
            @Nested
            class Context_with_invalid_token_and_valid_product {
                Long givenId = EXIST_ID;

                @DisplayName("401 코드를 응답한다")
                @Test
                void it_returns_401_code() throws Exception {
                    mockMvc.perform(
                            patch("/products/{id}", givenId)
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .header("Authorization", givenToken)
                                    .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                            "\"price\":5000}")
                    )
                            .andExpect(status().isUnauthorized());
                }
            }
        }
    }


    @DisplayName("DELETE /products/id")
    @Nested
    class Describe_DELETE_products_id {
        @DisplayName("유효한 access token과")
        @Nested
        class Context_with_valid_token {
            @DisplayName("존재하는 product id가 주어진다면")
            @Nested
            class Context_with_exist_product_id {
                Long givenId = EXIST_ID;
                String givenToken = "Bearer " + VALID_TOKEN;

                @DisplayName("204 코드를 응답한다")
                @Test
                void it_returns_200_code_with_product() throws Exception {
                    mockMvc.perform(
                            delete("/products/{id}", givenId)
                                    .header("Authorization", givenToken)
                    )
                            .andExpect(status().isNoContent());

                    verify(productService).deleteProduct(givenId);
                }
            }

            @DisplayName("존재하지 않는 product id가 주어진다면")
            @Nested
            class Context_with_not_exist_product_id {
                Long givenId = NOT_EXIST_ID;
                String givenToken = "Bearer " + VALID_TOKEN;

                @DisplayName("404코드를 응답한다")
                @Test
                void it_returns_404_code() throws Exception {
                    mockMvc.perform(
                            delete("/products/{id}", givenId)
                                    .header("Authorization", givenToken)
                    )
                            .andExpect(status().isNotFound());

                    verify(productService).deleteProduct(givenId);
                }
            }
        }

        @DisplayName("유효하지 않은 access token과")
        @Nested
        class Context_with_invalid_token {
            @DisplayName("존재하는 product id가 주어진다면")
            @Nested
            class Context_with_exist_product_id {
                Long givenId = EXIST_ID;
                String givenToken = "Bearer " + INVALID_TOKEN;

                @DisplayName("401 코드를 응답한다")
                @Test
                void it_returns_401_code() throws Exception {
                    mockMvc.perform(
                            delete("/products/{id}", givenId)
                                    .header("Authorization", givenToken)
                    )
                            .andExpect(status().isUnauthorized());
                }
            }
        }
    }
}
