package com.codesoom.assignment.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codesoom.assignment.application.JwtDecoder;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.interceptors.AuthenticationInterceptor;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@TestInstance(Lifecycle.PER_CLASS)
class ProductControllerTest {

    private static final String SECRET = "12345678901234567890123456789010";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjMwMTJ9.blzNDJtKA5NOZh6n4GfNVevWShBPGTCj6DBx48DfmUs";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjMwMTJ9.blzNDJtKA5NOZh6n4GfNVevWShBPGTCj6DBx48DfmU0";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private static JwtDecoder jwtDecoder;

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    private Product product;

    @BeforeEach
    void beforeAll() {
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

        given(jwtDecoder.decode(VALID_TOKEN))
            .willReturn(Optional.of(Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(VALID_TOKEN)
                .getBody())
            );
    }

    @BeforeEach
    void setUp() {
        product = Product.builder()
            .id(1L)
            .name("쥐돌이")
            .maker("냥이월드")
            .price(5000)
            .build();
    }

    @Nested
    @DisplayName("GET /products 요청은")
    class Describe_getProducts {

        @BeforeEach
        void setUp() {
            given(productService.getProducts()).willReturn(List.of(product));
        }

        @Test
        @DisplayName("모든 상품을 리턴하고 200을 응답한다")
        void it_returns_all_products_and_response_200() throws Exception {
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
    class Describe_getProductsWithId {

        @Nested
        @DisplayName("존재하는 상품일 경우")
        class Context_existProduct {

            @BeforeEach
            void setUp() {
                given(productService.getProduct(1L)).willReturn(product);
            }

            @Test
            @DisplayName("찾은 상품을 리턴하고 200을 응답한다")
            void it_returns_found_product_and_response_200() throws Exception {
                mockMvc.perform(
                    get("/products/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("쥐돌이")));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 상품인 경우")
        class Context_notExistProduct {

            @BeforeEach
            void setUp() {
                given(productService.getProduct(1000L))
                    .willThrow(new ProductNotFoundException(1000L));
            }

            @Test
            @DisplayName("404를 응답한다")
            void it_response_404() throws Exception {
                mockMvc.perform(get("/products/1000"))
                    .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("POST /products 요청은")
    class Describe_postProducts {

        @Nested
        @DisplayName("유효한 토큰일 경우")
        class Context_validToken {

            @Nested
            @DisplayName("유효한 상품이 주어지면")
            class Context_validProduct {

                @BeforeEach
                void setUp() {
                    given(productService.createProduct(any(ProductData.class)))
                        .willReturn(product);
                }

                @Test
                @DisplayName("생성한 상품을 리턴하고 201을 응답한다")
                void it_returns_created_product_and_response_201() throws Exception {
                    mockMvc.perform(
                        post("/products")
                            .accept(MediaType.APPLICATION_JSON_UTF8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
                            .header("Authorization", "Bearer " + VALID_TOKEN)
                    )
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString("쥐돌이")));

                    verify(productService).createProduct(any(ProductData.class));
                }
            }

            @Nested
            @DisplayName("유효하지 않은 상품이 주어지면")
            class Context_invalidProduct {

                @Test
                @DisplayName("400을 응답한다")
                void it_response_400() throws Exception {
                    mockMvc.perform(
                        post("/products")
                            .accept(MediaType.APPLICATION_JSON_UTF8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"\",\"maker\":\"\"," +
                                "\"price\":0}")
                            .header("Authorization", "Bearer " + VALID_TOKEN)
                    )
                        .andExpect(status().isBadRequest());
                }
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰일 경우")
        class Context_invalidToken {

            @Test
            @DisplayName("401을 응답한다")
            void it_response_401() throws Exception {
                mockMvc.perform(
                    post("/products")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                            "\"price\":5000}")
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
                )
                    .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("토큰이 없을 경우")
        class Context_withoutToken {

            @Test
            @DisplayName("401을 응답한다")
            void it_response_401() throws Exception {
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
    }

    @Nested
    @DisplayName("PATCH /products/{id} 요청은")
    class Describe_patchProductWithId {

        @Nested
        @DisplayName("유효한 토큰이 주어진 경우")
        class Context_validToken {

            @Nested
            @DisplayName("올바른 상품이 주어진 경우")
            class NestedClass {

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
                @DisplayName("수정한 상품을 리턴하고 200을 응답한다")
                void it_returns_updated_product_and_response_200() throws Exception {
                    mockMvc.perform(
                        patch("/products/1")
                            .accept(MediaType.APPLICATION_JSON_UTF8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
                            .header("Authorization", "Bearer " + VALID_TOKEN)
                    )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("쥐순이")));

                    verify(productService).updateProduct(eq(1L), any(ProductData.class));
                }
            }

            @Nested
            @DisplayName("올바르지 않은 상품이 주어진 경우")
            class Context_invalidProduct {

                @BeforeEach
                void setUp() {
                    given(productService.updateProduct(eq(1000L), any(ProductData.class)))
                        .willThrow(new ProductNotFoundException(1000L));
                }

                @Test
                @DisplayName("400을 응답한다")
                void it_response_400() throws Exception {
                    mockMvc.perform(
                        patch("/products/1")
                            .accept(MediaType.APPLICATION_JSON_UTF8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"\",\"maker\":\"\"," +
                                "\"price\":0}")
                            .header("Authorization", "Bearer " + VALID_TOKEN)
                    )
                        .andExpect(status().isBadRequest());
                }
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰일 경우")
        class Context_invalidToken {

            @Test
            @DisplayName("401을 응답한다")
            void it_response_401() throws Exception {
                mockMvc.perform(
                    patch("/products/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                            "\"price\":5000}")
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
                )
                    .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("토큰이 주어지지 않은 경우")
        class Context_withoutToken {

            @Test
            @DisplayName("401을 응답한다")
            void it_resopnse_401() throws Exception {
                mockMvc.perform(
                    patch("/products/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                            "\"price\":5000}")
                )
                    .andExpect(status().isUnauthorized());
            }
        }
    }


    @Nested
    @DisplayName("DELETE /products/{id} 요청은")
    class Describe_deleteProductWithId {

        @Nested
        @DisplayName("유효한 토큰이 주어진 경우")
        class Context_validToken {

            @Nested
            @DisplayName("존재하는 상품 id가 주어질 때")
            class Context_existProductId {

                @Test
                @DisplayName("204를 응답한다")
                void it_response_204() throws Exception {
                    mockMvc.perform(
                        delete("/products/1")
                            .header("Authorization", "Bearer " + VALID_TOKEN)
                    )
                        .andExpect(status().isNoContent());

                    verify(productService).deleteProduct(1L);
                }
            }

            @Nested
            @DisplayName("존재하지 않는 상품 id가 주어질 때")
            class Context_notExistProductId {

                @BeforeEach
                void setUp() {
                    given(productService.deleteProduct(1000L))
                        .willThrow(new ProductNotFoundException(1000L));
                }

                @Test
                @DisplayName("404를 응답한다")
                void it_response_404() throws Exception {
                    mockMvc.perform(
                        delete("/products/1000")
                            .header("Authorization", "Bearer " + VALID_TOKEN)
                    )
                        .andExpect(status().isNotFound());

                    verify(productService).deleteProduct(1000L);
                }
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰일 경우")
        class Context_invalidToken {

            @Test
            @DisplayName("401를 응답한다")
            void it_response_401() throws Exception {
                mockMvc.perform(
                    delete("/products/1")
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
                )
                    .andExpect(status().isUnauthorized());

                verify(productService, never()).deleteProduct(1L);
            }
        }

        @Nested
        @DisplayName("토큰이 주어지지 않은 경우")
        class Context_withoutToken {

            @Test
            @DisplayName("401를 응답한다")
            void it_response_401() throws Exception {
                mockMvc.perform(
                    delete("/products/1")
                )
                    .andExpect(status().isUnauthorized());

                verify(productService, never()).deleteProduct(1L);
            }
        }
    }
}
