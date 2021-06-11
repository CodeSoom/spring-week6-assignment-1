package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import javassist.NotFoundException;
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
import java.util.NoSuchElementException;

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

    private static final String INVALID_TOKEN = VALID_TOKEN + "INVALID";

    private static final Long ID = 1L;

    private static final String NAME = "쥐돌이";

    private static final String MAKER = "코드숨";

    private static final Integer PRICE = 9900;

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

        product = Product.builder()
                .id(ID)
                .name(NAME)
                .maker(MAKER)
                .price(PRICE)
                .build();

        given(productService.getProducts()).willReturn(List.of(product));
    }

    @AfterEach
    void resetAll() {
        reset(productService);
        reset(authenticationService);
    }

    @Nested
    @DisplayName("list 메서드")
    class DescribeGetProductList {
        @Test
        @DisplayName("모든 제품 목록을 반환한다")
        void list() throws Exception {
            mockMvc.perform(
                    get("/products")
                            .accept(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("쥐돌이")));
        }
    }

    @Nested
    @DisplayName("GET /products/:id 요청 시")
    class DescribeGetProduct {
        Long VALID_ID;
        Long INVALID_ID;

        @BeforeEach
        void setUp() throws Exception {
            List<Product> products = productService.getProducts();
            if (products.isEmpty()) {
                throw new NotFoundException("Empty products");
            }

            Product validProduct = products.get(0);
            VALID_ID = validProduct.getId();

            given(productService.getProduct(VALID_ID)).willReturn(validProduct);

            Product lastProduct = products.get(products.size() - 1);
            Product invalidProduct = Product.builder()
                    .id(lastProduct.getId() + 9999L)
                    .build();

            INVALID_ID = invalidProduct.getId();

            given(productService.getProduct(INVALID_ID))
                    .willThrow(new ProductNotFoundException(INVALID_ID));
        }

        @Nested
        @DisplayName("만약 유효한 식별자라면")
        class ContextWithValidId {
            @Test
            @DisplayName("해당 식별자의 제품을 반환한다")
            void itReturnsProduct() throws Exception {
                mockMvc.perform(
                        get("/products/" + ID)
                                .accept(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(NAME)));
            }
        }

        @Nested
        @DisplayName("만약 잘못된 식별자라면")
        class ContextWithInvalidId {
            @Test
            @DisplayName("NOT FOUND 응답 코드를 반환한다")
            void itReturnsNotFound() throws Exception {
                mockMvc.perform(get("/products/" + INVALID_ID))
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
        @DisplayName("유효한 인증 토큰이라면")
        class ContextWithValidAccessToken {
            @BeforeEach
            void setUp() {
                given(authenticationService.parseToken(VALID_TOKEN)).will(invocation -> {
                    String token = invocation.getArgument(0);
                    return new JwtUtil(JWT_KEY)
                            .decode(token)
                            .get("userId", Long.class);
                });
            }

            @Test
            @DisplayName("제품을 생성하고 CREATED 응답코드를 반환한다")
            void withValidAttributes() throws Exception {
                mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\",\"price\":5000}")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString("쥐돌이")));

                verify(productService).createProduct(any(ProductData.class));
            }
        }

        @Nested
        @DisplayName("잘못된 인증 토큰이라면")
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

    @Nested
    @DisplayName("PATCH /products/:id 요청 시")
    class DescribePatchProduct {
        Long VALID_ID;
        Long INVALID_ID;

        @BeforeEach
        void setUp() throws Exception {
            List<Product> products = productService.getProducts();
            if (products.isEmpty()) {
                throw new NotFoundException("Empty products");
            }

            Product validProduct = products.get(0);
            VALID_ID = validProduct.getId();

            given(productService.updateProduct(eq(VALID_ID), any(ProductData.class)))
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

            Product lastProduct = products.get(products.size() - 1);
            Product invalidProduct = Product.builder()
                    .id(lastProduct.getId() + 9999L)
                    .build();

            INVALID_ID = invalidProduct.getId();

            given(productService.updateProduct(eq(INVALID_ID), any(ProductData.class)))
                    .willThrow(new ProductNotFoundException(1000L));
        }

        @Nested
        @DisplayName("옳바른 요청 데이터")
        class ContextWithValidAttributes {
            @Test
            @DisplayName("해당 제품이 존재한다면, 갱신한 제품을 반환한다")
            void itReturnsUpdatedProductWithProduct() throws Exception {
                mockMvc.perform(
                        patch("/products/" + VALID_ID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\",\"price\":5000}")
                )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("쥐순이")));

                verify(productService).updateProduct(eq(VALID_ID), any(ProductData.class));
            }

            @Test
            @DisplayName("해당 제품이 없다면, NOT FOUND를 반환한다")
            void itReturnsNotFoundWithoutProduct() throws Exception {
                mockMvc.perform(
                        patch("/products/" + INVALID_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\",\"price\":5000}")
                )
                        .andExpect(status().isNotFound());

                verify(productService).updateProduct(eq(INVALID_ID), any(ProductData.class));
            }
        }

        @Nested
        @DisplayName("잘못된 요청 데이터")
        class ContextWithInvalidAttributes {
            @Test
            @DisplayName("BAD REQUEST를 반환한다")
            void itReturnsBadRequest() throws Exception {
                mockMvc.perform(
                        patch("/products/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"\",\"maker\":\"\",\"price\":0}")
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("DELETE /products/:id 요청 시")
    class DescribeDeleteProduct {
        Long VALID_ID;
        Long INVALID_ID;

        @BeforeEach
        void setUp() throws Exception {
            List<Product> products = productService.getProducts();
            if (products.isEmpty()) {
                throw new NotFoundException("Empty products");
            }

            Product validProduct = products.get(0);
            VALID_ID = validProduct.getId();

            Product lastProduct = products.get(products.size() - 1);
            Product invalidProduct = Product.builder()
                    .id(lastProduct.getId() + 9999L)
                    .build();

            INVALID_ID = invalidProduct.getId();

            given(productService.deleteProduct(INVALID_ID))
                    .willThrow(new ProductNotFoundException(INVALID_ID));
        }

        @Nested
        @DisplayName("해당 제품이 존재한다면")
        class ContextWithExistedProduct {
            @Test
            @DisplayName("제품을 삭제한다.")
            void destroyProduct() throws Exception {
                mockMvc.perform(
                        delete("/products/" + VALID_ID)
                )
                        .andExpect(status().isNoContent());

                verify(productService).deleteProduct(eq(VALID_ID));
            }
        }

        @Nested
        @DisplayName("해당 제품이 없다면")
        class ContextWithNotExistedProduct {
            @Test
            @DisplayName("NOT FOUND를 반환한다")
            void itReturnsNotFound() throws Exception {
                mockMvc.perform(
                        delete("/products/" + INVALID_ID)
                )
                        .andExpect(status().isNotFound());

                verify(productService).deleteProduct(eq(INVALID_ID));
            }
        }
    }
}
