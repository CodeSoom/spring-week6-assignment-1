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

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = VALID_TOKEN + "invalid";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

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

        given(authenticationService.parseToken(VALID_TOKEN)).willReturn(1L);

        given(authenticationService.parseToken(INVALID_TOKEN))
                .willThrow(new InvalidTokenException(INVALID_TOKEN));

        given(authenticationService.parseToken(null))
                .willThrow(new InvalidTokenException(null));
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

    @Test
    void createWithoutAccessToken() throws Exception {
        mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                )
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void createWithWrongAccessToken() throws Exception {
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

    @Test
    void createWithValidAttributes() throws Exception {
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

    @Test
    void createWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"maker\":\"\"," +
                                "\"price\":0}")
                        .header("Authorization", "Bearer " + VALID_TOKEN) // header만 있으면 테스트 통과..?
        )
                .andExpect(status().isBadRequest());
    }


    /*
    계층형으로 작성하여 테스트할 때 통과되지 않음.
    com.codesoom.assignment.errors.ProductNotFoundException: Product not found: 1000
	at com.codesoom.assignment.controllers.ProductControllerTest.setUp(ProductControllerTest.java:58)
     */
    @Nested
    @DisplayName("update메소드는")
    class Describe_update{

        @Nested
        @DisplayName("토큰 정보가 없이 요청받았을 때")
        class Context_without_token{

            @DisplayName("unauthorized 상태를 응답한다.")
            @Test
            void it_response_unauthorized_status() throws Exception {
                mockMvc.perform(
                        patch("/products/1")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                )
                .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰으로 요청받았을 때")
        class Context_whit_invalid_token{

            @DisplayName("unauthorized 상태를 응답한다.")
            @Test
            void it_response_unauthorized_status() throws Exception {
                mockMvc.perform(
                        patch("/products/1")
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
        @DisplayName("유효한 토큰 정보와 함께 요청받았을 때")
        class Context_whit_valid_token{

            @Nested
            @DisplayName("존재하는 상품에 대한 요청일 경우")
            class Context_with_existed_product{

                @Test
                @DisplayName("ok 상태를 응답한다.")
                void it_responses_ok_status() throws Exception {
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
            @DisplayName("존재하지 않는 상품에 대한 요청일 경우")
            class Context_with_not_existed_product{

                @Test
                @DisplayName("notFound 상태를 응답한다.")
                void it_responses_status_notFound() throws Exception {
                    mockMvc.perform(
                                patch("/products/1000")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                                "\"price\":5000}")
                                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        )
                            .andExpect(status().isNotFound());

                    verify(productService).updateProduct(eq(1000L), any(ProductData.class));
                }
            }

            @Nested
            @DisplayName("파라미터 속성이 유효하지 않는 경우")
            class Context_WithInvalidAttributes{

                @Test
                @DisplayName("BadRequest 상태를 응답한다.")
                void it_responses_status_badRequest() throws Exception {
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
    }

    //토근 정보가 유효할 때
    @Test
    void destroyWithExistedProduct() throws Exception {
        mockMvc.perform(
                delete("/products/1")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }

    @Test
    void destroyWithNotExistedProduct() throws Exception {
        mockMvc.perform(
                delete("/products/1000")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isNotFound());

        verify(productService).deleteProduct(1000L);
    }

    //토큰 정보가 유효하지 않을 때
    @Test
    void destroyWithInvalidToken() throws Exception {
        mockMvc.perform(
                        delete("/products/1000")
                                .header("Authorization", "Bearer " + INVALID_TOKEN)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void destroyWithoutToken() throws Exception {
        mockMvc.perform(
                        delete("/products/1000")
                )
                .andExpect(status().isUnauthorized());
    }
}
