package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@DisplayName("ProductController 테스트")
class ProductControllerTest {
    private static final String JWT_KEY = "12345678901234567890123456789010";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpK0INVALID";

    private static final Long PRODUCT_ID = 1L;
    private static final String PRODUCT_NAME = "쥐냥이";
    private static final String PRODUCT_MAKER = "쥐냥이";
    private static final Integer PRODUCT_PRICE = 9900;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    private Product product;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();

        product = productGenerator();

        given(productService.getProducts()).willReturn(List.of(product));

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
    }

    @AfterEach
    void resetAll() {
        reset(productService);
        reset(authenticationService);
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(
                get("/products")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")));
    }

    @Nested
    @DisplayName("GET /products/:id 요청 시")
    class DescribeGetProduct {
        @Nested
        @DisplayName("만약 유효한 식별자라면")
        class ContextWithValidId {
            @BeforeEach
            void setUp() {
                given(productService.getProduct(any())).willReturn(product);
            }

            @Test
            @DisplayName("해당 식별자의 제품을 반환한다")
            void itReturnsProduct() throws Exception {
                mockMvc.perform(
                        get("/products/1")
                                .accept(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("쥐돌이")));
            }
        }

        @Nested
        @DisplayName("만약 잘못된 식별자라면")
        class ContextWithInvalidId {
            @BeforeEach
            void setUp() {
                given(productService.getProduct(eq(1000L)))
                        .willThrow(new ProductNotFoundException(1000L));
            }

            @Test
            @DisplayName("NOT FOUND 응답 코드를 반환한다")
            void itReturnsNotFound() throws Exception {
                mockMvc.perform(get("/products/1000"))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("POST /products 요청 시")
    class DescribeCreateProduct {
        @BeforeEach
        void setUp() {
            given(productService.createProduct(any(ProductData.class)))
                    .willReturn(product);
        }

        @Nested
        @DisplayName("만약 유효한 인증 토큰이라면")
        class ContextWithValidAccessToken {
            @BeforeEach
            void setUp() {
                given(authenticationService.parseToken(VALID_TOKEN)).will(invocation -> {
                    String token = invocation.getArgument(0);
                    return new JwtUtil(JWT_KEY)
                            .decode(token);
                });
            }

            @Test
            @DisplayName("제품을 생성하고 CREATED 응답코드를 반환한다")
            void withValidAttributes() throws Exception {
                mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON)
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
        @DisplayName("만약 잘못된 인증 토큰이라면")
        class ContextWithoutValidAccessToken {
            @BeforeEach
            void setUp() {
                given(authenticationService.parseToken(INVALID_TOKEN))
                        .willThrow(new InvalidTokenException(INVALID_TOKEN));
            }

            @Test
            @DisplayName("UNAUTHORIZED 응답코드를 반환한다")
            void withInvalidAccessToken() throws Exception {
                mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer " + INVALID_TOKEN)
                )
                        .andExpect(status().isUnauthorized());
            }

            @Test
            @DisplayName("UNAUTHORIZED 응답코드를 반환한다")
            void withoutAccessToken() throws Exception {
                mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                )
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("만약 잘못된 요청 파라미터라면")
        class ContextWithInvalidAttributes {
            @Test
            @DisplayName("BAD REQUEST 응답코드를 반환한다")
            void withValidAttributes() throws Exception {
                mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"\",\"maker\":\"\"," +
                                        "\"price\":0}")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Test
    void updateWithExistedProduct() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
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
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"maker\":\"\"," +
                                "\"price\":0}")
        )
                .andExpect(status().isBadRequest());
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

    public Product productGenerator() {
        return Product.builder()
                .id(PRODUCT_ID)
                .name(PRODUCT_NAME)
                .maker(PRODUCT_MAKER)
                .price(PRODUCT_PRICE)
                .build();
    }
}
