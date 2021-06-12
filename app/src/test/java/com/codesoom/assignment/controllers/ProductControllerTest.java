package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@DisplayName("Describe: ProductController 테스트")
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
    AuthenticationService authenticationService;

    Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .build();
    }

//        given(productService.updateProduct(eq(1L), any(ProductData.class)))
//                .will(invocation -> {
//                    Long id = invocation.getArgument(0);
//                    ProductData productData = invocation.getArgument(1);
//                    return Product.builder()
//                            .id(id)
//                            .name(productData.getName())
//                            .maker(productData.getMaker())
//                            .price(productData.getPrice())
//                            .build();
//                });
//
//        given(productService.updateProduct(eq(1000L), any(ProductData.class)))
//                .willThrow(new ProductNotFoundException(1000L));
//
//        given(productService.deleteProduct(1000L))
//                .willThrow(new ProductNotFoundException(1000L));
//
//        given(authenticationService.parseToken(VALID_TOKEN)).willReturn(1L);
//
//        given(authenticationService.parseToken(INVALID_TOKEN)).willThrow(new InvalidTokenException(INVALID_TOKEN));
//    }

    @Nested
    @DisplayName("Describe: 장난감을 요청할 때")
    class DescribeGetProduct {

        @Nested
        @DisplayName("Context: 전체 목록을 요청한다면")
        class ContextGetAllProducts {
            @BeforeEach
            void setUp() {
                given(productService.getProducts()).willReturn(List.of(product));

            }

            @Test
            @DisplayName("It: 모든 장난감 목록을 리턴한다.")
            void list() throws Exception {
                mockMvc.perform(
                        get("/products")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("쥐돌이")));
            }
        }

        @Nested
        @DisplayName("Context: 목록에 존재하는 장난감을 요청한다면")
        class ContextWithExistedProduct {

            @BeforeEach
            void setUp() {
                given(productService.getProduct(1L)).willReturn(product);

            }

            @Test
            @DisplayName("It: 해당 장난감을 리턴한다.")
            void detailWithExistedProduct() throws Exception {
                mockMvc.perform(
                        get("/products/1")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("쥐돌이")));
            }
        }

        @Nested
        @DisplayName("Context: 목록에 없는 장난감을 요청한다면")
        class ContextWithNotExistedProduct {

            @BeforeEach
            void setUp() {
                given(productService.getProduct(1000L))
                        .willThrow(new ProductNotFoundException(1000L));
            }

            @Test
            @DisplayName("It: 장난감을 찾을 수 없습니다.")
            void detailWithNotExistedProduct() throws Exception {
                mockMvc.perform(get("/products/1000"))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("Describe: 장난감을 새로 등록할 때,")
    class DescribeCreateProduct {

        @BeforeEach
        void setUp() {
            given(productService.createProduct(any(ProductData.class)))
                    .willReturn(product);
        }

        @Nested
        @DisplayName("Context: AccessToken 이 존재한다면,")
        class ContextWithAccessToken {
            ResultActions mock;

            @BeforeEach
            void setUp() throws Exception {
                mock = mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer" + VALID_TOKEN)
                );

                given(authenticationService.parseToken(VALID_TOKEN)).willReturn(1L);

            }

            @Test
            @DisplayName("It: 장난감이 정상적으로 등록된다.")
            void createNewToy() throws Exception {
                mock.andExpect(status().isCreated())
                        .andExpect(content().string(containsString("쥐돌이")));

                verify(productService).createProduct(any(ProductData.class));
            }
        }

        @Nested
        @DisplayName("Context: AccessToken 이 존재하여도, 입력된 장난감 정보가 잘못 됐다면,")
        class ContextAccessTokenWithWrongAttributes {
            ResultActions mock;

            @BeforeEach
            void setUp() throws Exception {
                mock = mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"\",\"maker\":\"\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer" + VALID_TOKEN)
                );

                given(authenticationService.parseToken(VALID_TOKEN)).willReturn(1L);
            }

            @Test
            @DisplayName("It: 장난감이 등록할 수 없다.")
            void createNewToy() throws Exception {
                mock.andExpect(status().isBadRequest());

            }
        }


        @Nested
        @DisplayName("Context: AccessToken 이 없다면,")
        class ContextWithNoAccessToken {

            @Test
            @DisplayName("It: 새로운 장난감을 등록할 수 없다.")
            void createWithoutAccessToken() throws Exception {
                mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                )
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("Context: 유효하지 않은 AccessToken 으로 접근한다면,")
        class ContextWithWrongAccessToken {

            @BeforeEach
            void setUp() {
                given(authenticationService.parseToken(INVALID_TOKEN)).willThrow(new InvalidTokenException(INVALID_TOKEN));
            }

            @Test
            @DisplayName("It: 새로운 장난감을 등록할 수 없다.")
            void createWithWrongAccessToken() throws Exception {
                mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer" + INVALID_TOKEN)
                )
                        .andExpect(status().isUnauthorized());
            }
        }
    }


    @Nested
    @DisplayName("장난감 정보를 수정할 때")
    class TestUpdateProduct {

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

            given(productService.updateProduct(eq(1000L), any(ProductData.class)))
                    .willThrow(new ProductNotFoundException(1000L));
        }

        @Nested
        @DisplayName("Describe: 인증된 사용자라면,")
        class DescribeWithAuthorizedUser {

            @BeforeEach
            void setUp() {
                given(authenticationService.parseToken(VALID_TOKEN)).willReturn(1L);
            }

            @Nested
            @DisplayName("Context: 등록된 장난감을 수정할 때")
            class ContextWithExistedId {
                @Test
                @DisplayName("It: 정보를 수정할 수 있다.")
                void updateProduct() throws Exception {
                    mockMvc.perform(
                            patch("/products/1")
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                            "\"price\":5000}")
                                    .header("Authorization", "Bearer" + VALID_TOKEN)

                    )
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString("쥐순이")));
                }
            }


            @Nested
            @DisplayName("Context: 등록되지 않은 장난감을 수정할 때")
            class ContextWithNotExistedId {
                @Test
                @DisplayName("It: 정보를 수정할 수 없다.")
                void updateProduct() throws Exception {
                    mockMvc.perform(
                            patch("/products/1000")
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                            "\"price\":5000}")
                                    .header("Authorization", "Bearer" + VALID_TOKEN)

                    )
                            .andExpect(status().isNotFound());
                }
            }
        }

        @Nested
        @DisplayName("Context: 인증되지 않은 사용자라면,")
        class ContextWithUnauthorizedUser {

            @BeforeEach
            void setUp() {
                given(authenticationService.parseToken(INVALID_TOKEN)).willThrow(new InvalidTokenException(INVALID_TOKEN));
            }

            @Test
            @DisplayName("It: 정보를 수정할 수 없다.")
            void updateProduct() throws Exception {
                mockMvc.perform(
                        patch("/products/1000")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer" + INVALID_TOKEN)

                )
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Test
    void destroyWithExistedProduct() throws Exception {
        mockMvc.perform(
                delete("/products/1")
        )
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }

    @Test
    void destroyWithNotExistedProduct() throws Exception {
        mockMvc.perform(
                delete("/products/1000")
        )
                .andExpect(status().isNotFound());

        verify(productService).deleteProduct(1000L);
    }
}
