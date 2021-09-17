package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@DisplayName("ProductController 테스트")
class ProductControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    private static final String SECRET = "12345678901234567890123456789010";
    private static final Long VALID_ID = 1L;
    private String valid_token;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .build();

        JwtUtil jwtUtil = new JwtUtil(SECRET);
        valid_token = jwtUtil.encode(VALID_ID);

        given(productService.getProducts()).willReturn(List.of(product));

        given(productService.getProduct(1L)).willReturn(product);

        willThrow(new ProductNotFoundException(1000L)).given(productService).getProduct(1000L);

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

        willThrow(new ProductNotFoundException(1000L)).given(productService).deleteProduct(1000L);
    }

    @Nested
    @DisplayName("GET /products 호출")
    class Describe_get_path_products {

        @Test
        @DisplayName("status: Ok data: 모든 products 를 반환합니다.")
        void it_response_ok() throws Exception {
            mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("쥐돌이")));
        }
    }


    @Nested
    @DisplayName("GET /products/{id} 호출")
    class Describe_get_path_products_id {

        @Nested
        @DisplayName("저장된 상품을 전달하면")
        class Context_with_exsited_product {

            @Test
            @DisplayName("status: Ok data: product 반환합니다.")
            void it_response_is_ok() throws Exception {
                mockMvc.perform(get("/products/1"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("쥐돌이")));
            }
        }

        @Nested
        @DisplayName("저장되지 않은 상품을 전달하면")
        class Context_with_not_exsited_product {

            @Test
            @DisplayName("status: NotFound 반환합니다.")
            void it_response_not_found() throws Exception {
                mockMvc.perform(get("/products/1000"))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("POST /products 호출")
    class Describe_post_path_products {

        @Nested
        @DisplayName("올바른 product 정보를 주어지면")
        class Context_with_valid_attributes {

            @Test
            @DisplayName("status: created data: 생성된 product 를 반환합니다.")
            void it_response_created() throws Exception {
                mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                )
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString("쥐돌이")));

                verify(productService).createProduct(any(ProductData.class));
            }
        }

        @Nested
        @DisplayName("유효하지 않은 product 정보가 주어지면")
        class Context_with_invalid_attributes {

            @Test
            @DisplayName("status: Bad request 를 반환합니다.")
            void it_response_bad_request() throws Exception {
                mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"\",\"maker\":\"\"," +
                                        "\"price\":0}")
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("PATCH /products/{id} 호출")
    class Describe_patch_path_products_id {

        @Nested
        @DisplayName("저장된 product 정보가 주어지면")
        class Context_with_existed_product {

            @Test
            @DisplayName("status: ok data: 수정된 product 를 반환합니다.")
            void it_response_ok() throws Exception {
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
        }

        @Nested
        @DisplayName("저장되지 않은 product 정보가 주어지면")
        class Context_with_not_existed_product {

            @Test
            @DisplayName("status: Not found 를 반환합니다.")
            void it_response_bad_request() throws Exception {
                mockMvc.perform(
                        patch("/products/1000")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                )
                        .andExpect(status().isNotFound());

                verify(productService).updateProduct(eq(1000L), any(ProductData.class));
            }
        }

        @Nested
        @DisplayName("유효하지 않는 product 정보가 주어지면")
        class Context_with_invalid_attributes {

            @Test
            @DisplayName("status: Bad Request 를 반환합니다.")
            void it_response_bad_request() throws Exception {
                mockMvc.perform(
                        patch("/products/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"\",\"maker\":\"\"," +
                                        "\"price\":0}")
                )
                        .andExpect(status().isBadRequest());
            }
        }

    }

    @Nested
    @DisplayName("DELETE /products/{id} 호출")
    class Describe_delete_path_products_id {

        @Nested
        @DisplayName("저장된 product 정보를 주어지면")
        class Context_with_existed_product {

            @Test
            @DisplayName("status: No content 를 반환합니다.")
            void it_response_created() throws Exception {
                mockMvc.perform(
                        delete("/products/1")
                )
                        .andExpect(status().isNoContent());

                verify(productService).deleteProduct(1L);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 product 정보가 주어지면")
        class Context_with_not_existed_products {

            @Test
            @DisplayName("status: Not found 를 반환합니다.")
            void it_response_bad_request() throws Exception {
                mockMvc.perform(
                        delete("/products/1000")
                )
                        .andExpect(status().isNotFound());

                verify(productService).deleteProduct(1000L);
            }
        }
    }
}
