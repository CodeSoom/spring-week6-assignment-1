package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.codesoom.assignment.utils.TestHelper.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@SuppressWarnings({"InnerClassMayBeStatic", "NonAsciiCharacters"})
@DisplayName("ProductController 클래스")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtUtil jwtUtil;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class list_메서드는 {

        @BeforeEach
        void setUp() {
            given(productService.getProducts()).willReturn(List.of(TEST_PRODUCT));
        }

        @Test
        @DisplayName("상품목록을 반환한다")
        void It_returns_product_list() throws Exception {
            mockMvc.perform(get("/products")
                            .accept(MediaType.APPLICATION_JSON_UTF8)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString(TEST_PRODUCT_NAME)));
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class detail_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 요청받은_id에_해당하는_상품이_존재하는_경우 {
            @BeforeEach
            void setUp() {
                given(productService.getProduct(1L)).willReturn(TEST_PRODUCT);
            }

            @Test
            @DisplayName("해당 id의 상품을 반환한다")
            void It_returns_product() throws Exception {
                mockMvc.perform(get("/products/1")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(TEST_PRODUCT_NAME)));
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 요청받은_id에_해당하는_상품이_존재하지_않는_경우 {
            @BeforeEach
            void setUp() {
                given(productService.getProduct(1000L))
                        .willThrow(new ProductNotFoundException(1000L));
            }

            @Test
            @DisplayName("에러메시지를 반환한다")
            void It_returns_404_error() throws Exception {
                mockMvc.perform(get("/products/1000")
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().isNotFound())
                        .andDo(print());
            }
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class create_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효한_토큰을_전달받은_경우 {
            @BeforeEach
            void setUp() {
                given(jwtUtil.decode(VALID_TOKEN)).will(invocation -> {
                    String accessToken = invocation.getArgument(0);
                    Claims claims = new JwtUtil("12345678901234567890123456789010").decode(accessToken);
                    return claims;
                });
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 상품_요청이_정상적인_경우 {
                @BeforeEach
                void setUp() {
                    given(productService.createProduct(any(ProductData.class)))
                            .willReturn(TEST_PRODUCT);
                }

                @Test
                @DisplayName("상품을 생성하고, 생성된 상품을 반환한다")
                void It_creates_product_and_returns_it() throws Exception {
                    mockMvc.perform(post("/products")
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .content(objectMapper.writeValueAsString(TEST_PRODUCT_DATA)))
                            .andExpect(status().isCreated())
                            .andExpect(content().string(containsString(TEST_PRODUCT_NAME)));
                }
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 상품요청이_비정상적인_경우 {

                @ParameterizedTest
                @MethodSource("com.codesoom.assignment.utils.TestHelper#provideInvalidProductRequests")
                @DisplayName("에러메시지를 반환한다")
                void It_returns_400_error() throws Exception {
                    mockMvc.perform(post("/products")
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                            .andExpect(status().isBadRequest())
                            .andDo(print());
                }
            }

        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효하지_않은_토큰을_전달받은_경우 {
            @BeforeEach
            void setUp() {
                Mockito.reset(jwtUtil);
                given(jwtUtil.decode(INVALID_TOKEN)).willThrow(new InvalidAccessTokenException());
            }

            @Test
            @DisplayName("에러메시지를 반환한다")
            void It_returns_401_error() throws Exception {
                mockMvc.perform(post("/products")
                                .header("Authorization", "Bearer " + INVALID_TOKEN)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(TEST_PRODUCT_DATA)))
                        .andExpect(status().isUnauthorized())
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class update_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효한_토큰을_전달받은_경우 {
            @BeforeEach
            void setUp() {
                given(jwtUtil.decode(VALID_TOKEN)).will(invocation -> {
                    String accessToken = invocation.getArgument(0);
                    Claims claims = new JwtUtil("12345678901234567890123456789010").decode(accessToken);
                    return claims;
                });
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 상품_요청이_정상적인_경우 {
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
                @DisplayName("상품을 수정하고, 수정된 상품을 반환한다")
                void It_updates_product_and_returns_it() throws Exception {
                    mockMvc.perform(patch("/products/1")
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .content(objectMapper.writeValueAsString(UPDATE_PRODUCT_DATA)))
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString(TEST_UPDATE_PRODUCT_NAME)));
                }
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 상품요청이_비정상적인_경우 {
                @BeforeEach
                void setUp() {
                    given(jwtUtil.decode(VALID_TOKEN)).willReturn(null);
                }

                @ParameterizedTest
                @MethodSource("com.codesoom.assignment.utils.TestHelper#provideInvalidProductRequests")
                @DisplayName("에러메시지를 반환한다")
                void It_returns_400_error() throws Exception {
                    mockMvc.perform(patch("/products/1")
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                            .andExpect(status().isBadRequest())
                            .andDo(print());
                }
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 수정할_상품_정보가_없는경우 {
                @BeforeEach
                void setUp() {
                    given(productService.updateProduct(eq(1000L), any(ProductData.class)))
                            .willThrow(new ProductNotFoundException(1000L));
                }

                @Test
                @DisplayName("에러메시지를 반환한다")
                void It_returns_404_error() throws Exception {
                    mockMvc.perform(patch("/products/1000")
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .content(objectMapper.writeValueAsString(UPDATE_PRODUCT_DATA)))
                            .andExpect(status().isNotFound())
                            .andDo(print());
                }
            }

        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효하지_않은_토큰을_전달받은_경우 {
            @BeforeEach
            void setUp() {
                Mockito.reset(jwtUtil);
                given(jwtUtil.decode(INVALID_TOKEN)).willThrow(new InvalidAccessTokenException());
            }

            @Test
            @DisplayName("에러메시지를 반환한다")
            void It_returns_401_error() throws Exception {
                mockMvc.perform(patch("/products/1")
                                .header("Authorization", "Bearer " + INVALID_TOKEN)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(UPDATE_PRODUCT_DATA)))
                        .andExpect(status().isUnauthorized())
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class destroy_메서드는 {
        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효한_토큰을_전달받은_경우 {
            @BeforeEach
            void setUp() {
                given(jwtUtil.decode(VALID_TOKEN)).will(invocation -> {
                    String accessToken = invocation.getArgument(0);
                    Claims claims = new JwtUtil("12345678901234567890123456789010").decode(accessToken);
                    return claims;
                });
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 상품_삭제요청이_정상적인_경우 {
                @Test
                @DisplayName("상품을 삭제한다")
                void It_delete_product() throws Exception {
                    mockMvc.perform(delete("/products/1")
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .accept(MediaType.APPLICATION_JSON_UTF8)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .content(objectMapper.writeValueAsString(UPDATE_PRODUCT_DATA)))
                            .andExpect(status().isNoContent());
                }
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 삭제할_상품_정보가_없는경우 {
                @BeforeEach
                void setUp() {
                    given(productService.deleteProduct(1000L))
                            .willThrow(new ProductNotFoundException(1000L));
                }

                @Test
                @DisplayName("에러메시지를 반환한다")
                void It_returns_404_error() throws Exception {
                    mockMvc.perform(delete("/products/1000")
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                            .andExpect(status().isNotFound())
                            .andDo(print());
                }
            }

        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효하지_않은_토큰을_전달받은_경우 {
            @BeforeEach
            void setUp() {
                Mockito.reset(jwtUtil);
                given(jwtUtil.decode(INVALID_TOKEN)).willThrow(new InvalidAccessTokenException());
            }

            @Test
            @DisplayName("에러메시지를 반환한다")
            void It_returns_401_error() throws Exception {
                mockMvc.perform(delete("/products/1")
                                .header("Authorization", "Bearer " + INVALID_TOKEN)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().isUnauthorized())
                        .andDo(print());
            }
        }
    }
}
