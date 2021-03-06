package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.utils.JWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    private final String sessionToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
            ".eyJpZCI6MSwibmFtZSI6Imp1dW5pbmkiLCJlbWFpbCI6Imp1dW5pLm5pLmlAZ21haWwuY29tIn0" +
            ".xlmA0uvH66lyfxT3H5uR1M_1LfmqVK2pGQGJHTom9qg";
    private final String invalidSessionToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
            ".eyJpZCI6MiwibmFtZSI6Imp1dW5pbmkiLCJlbWFpbCI6Imp1dW5pLm5pLmlAZ21haWwuY29tIn0" +
            ".J7kLVUDUbf-5xZ8c027YGIW2uyJXeQYLzgnlmJa9oT0";

    @MockBean
    private JWT jwt;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthService authService;

    @BeforeEach
    void setUp() {
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

        final JWT realJWT = new JWT("12345678901234567890123456789010");

        given(jwt.decode(sessionToken)).willReturn(realJWT.decode(sessionToken));
        given(jwt.decode(invalidSessionToken)).willReturn(realJWT.decode(invalidSessionToken));
        given(authService.valid(1L, "juuni.ni.i@gmail.com", "juunini")).willReturn(true);
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(
                get("/products")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")));
    }

    @Test
    void deatilWithExsitedProduct() throws Exception {
        mockMvc.perform(
                get("/products/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")));
    }

    @Test
    void deatilWithNotExsitedProduct() throws Exception {
        mockMvc.perform(get("/products/1000"))
                .andExpect(status().isNotFound());
    }

    @Nested
    @DisplayName("[POST] /products 요청은")
    class Describe_post_products_request {
        @Test
        void createWithValidAttributes() throws Exception {
            mockMvc.perform(
                    post("/products")
                            .accept(MediaType.APPLICATION_JSON_UTF8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + sessionToken)
                            .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                    "\"price\":5000}")
            )
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString("쥐돌이")));

            verify(productService).createProduct(any(ProductData.class));
        }

        @Test
        void createWithInvalidAttributes() throws Exception {
            mockMvc.perform(
                    post("/products")
                            .accept(MediaType.APPLICATION_JSON_UTF8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + sessionToken)
                            .content("{\"name\":\"\",\"maker\":\"\"," +
                                    "\"price\":0}")
            )
                    .andExpect(status().isBadRequest());
        }

        @Nested
        @DisplayName("올바른 세션이 아닐 때")
        class Context_without_valid_session {
            @Test
            @DisplayName("bad request 를 응답한다.")
            void It_respond_bad_request() throws Exception {
                mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + invalidSessionToken)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("[PATCH] /products/{id} 요청은")
    class Describe_patch_products_request {
        @Test
        void updateWithExistedProduct() throws Exception {
            mockMvc.perform(
                    patch("/products/1")
                            .accept(MediaType.APPLICATION_JSON_UTF8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + sessionToken)
                            .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                    "\"price\":5000}")
            )
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("쥐순이")));

            verify(productService).updateProduct(eq(1L), any(ProductData.class));
        }

        @Test
        void updateWithNotExistedProduct() throws Exception {
            mockMvc.perform(
                    patch("/products/1000")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + sessionToken)
                            .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                    "\"price\":5000}")
            )
                    .andExpect(status().isNotFound());

            verify(productService).updateProduct(eq(1000L), any(ProductData.class));
        }

        @Test
        void updateWithInvalidAttributes() throws Exception {
            mockMvc.perform(
                    patch("/products/1")
                            .accept(MediaType.APPLICATION_JSON_UTF8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + sessionToken)
                            .content("{\"name\":\"\",\"maker\":\"\"," +
                                    "\"price\":0}")
            )
                    .andExpect(status().isBadRequest());
        }

        @Nested
        @DisplayName("올바른 세션이 아닐 때")
        class Context_without_valid_session {
            @Test
            @DisplayName("bad request 를 응답한다.")
            void It_respond_bad_request() throws Exception {
                mockMvc.perform(
                        patch("/products/1")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + invalidSessionToken)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("[DELETE] /products/{id} 요청은")
    class Describe_delete_products_request {
        @Test
        void destroyWithExistedProduct() throws Exception {
            mockMvc.perform(
                    delete("/products/1")
                            .header("Authorization", "Bearer " + sessionToken)
            )
                    .andExpect(status().isNoContent());

            verify(productService).deleteProduct(1L);
        }

        @Test
        void destroyWithNotExistedProduct() throws Exception {
            mockMvc.perform(
                    delete("/products/1000")
                            .header("Authorization", "Bearer " + sessionToken)
            )
                    .andExpect(status().isNotFound());

            verify(productService).deleteProduct(1000L);
        }

        @Nested
        @DisplayName("올바른 세션이 아닐 때")
        class Context_without_valid_session {
            @Test
            @DisplayName("bad request 를 응답한다.")
            void It_respond_bad_request() throws Exception {
                mockMvc.perform(
                        delete("/products/1")
                                .header("Authorization", "Bearer " + invalidSessionToken)
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
