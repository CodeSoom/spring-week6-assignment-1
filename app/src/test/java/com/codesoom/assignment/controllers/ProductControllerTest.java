package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthorizationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.fixture.FixtureData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    private static final String VALID_TOKEN = FixtureData.AccessTokenFixture.VALID_TOKEN.getToken();
    private static final String SIGNATURE_FAIL_TOKEN = FixtureData.AccessTokenFixture.SIGNATURE_FAIL_TOKEN.getToken();
    public final String USER_NOT_EXISTS_TOKEN = FixtureData.AccessTokenFixture.USER_NOT_EXISTS_TOKEN.getToken();
    public Long INVALID_TOKEN_USER_ID = FixtureData.AccessTokenFixture.USER_NOT_EXISTS_TOKEN.getUserId();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthorizationService authorizationService;

    @BeforeEach
    void setUp() {
        Product product = Product.builder()
                .id(1L)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .build();
        given(productService.getProducts()).willReturn(Collections.singletonList(product));

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

        doNothing().when(authorizationService).checkUserAuthorization("Bearer " + VALID_TOKEN);
        doThrow(new InvalidAccessTokenException("Bearer " + SIGNATURE_FAIL_TOKEN)).when(authorizationService).checkUserAuthorization("Bearer " + SIGNATURE_FAIL_TOKEN);
        doThrow(new InvalidAccessTokenException("")).when(authorizationService).checkUserAuthorization("Bearer ");
        doThrow(new InvalidAccessTokenException(USER_NOT_EXISTS_TOKEN, new UserNotFoundException(INVALID_TOKEN_USER_ID))).when(authorizationService).checkUserAuthorization("Bearer " + USER_NOT_EXISTS_TOKEN);
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
    void detailWithExitedProduct() throws Exception {
        mockMvc.perform(
                get("/products/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")));
    }

    @Test
    void detailWithNotExitedProduct() throws Exception {
        mockMvc.perform(get("/products/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Description("header에 authoirization 없으면_isUnauthorized")
    void createWithoutAuthorization() throws Exception {
        mockMvc.perform(
                post("/products")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                        "\"price\":5000}")
            ).andExpect(status().isUnauthorized());
    }

    @Test
    @Description("빈 authorization token_isUnauthorized")
    void createWithBlankAuthorization() throws Exception {
        mockMvc.perform(
            post("/products")
                .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                    "\"price\":5000}")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @Description("정상 authorization_isCreated")
    void createWithValidAuthorization() throws Exception {
        mockMvc.perform(
            post("/products")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                    "\"price\":5000}")
        ).andExpect(status().isCreated());

        verify(authorizationService).checkUserAuthorization(any());
    }

    @Test
    @Description("없는유저authorization_401")
    void createWithInvalidUserAuthorization() throws Exception {
        mockMvc.perform(
                post("/products")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + USER_NOT_EXISTS_TOKEN)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
        ).andExpect(status().isUnauthorized());

        verify(authorizationService).checkUserAuthorization(any());
    }

    @Test
    @Description("비정상 authorization_isUnauthorized")
    void createWithInvalidAuthorization() throws Exception {
        mockMvc.perform(
            post("/products")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SIGNATURE_FAIL_TOKEN)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                    "\"price\":5000}")
        ).andExpect(status().isUnauthorized());

        verify(authorizationService).checkUserAuthorization(any());
    }

    @Test
    void createWithValidAttributes() throws Exception {
        mockMvc.perform(
                post("/products")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
        )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("쥐돌이")));

        verify(authorizationService).checkUserAuthorization(any());
        verify(productService).createProduct(any(ProductData.class));
    }

    @Test
    void createWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                post("/products")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"maker\":\"\"," +
                                "\"price\":0}")
        )
                .andExpect(status().isBadRequest());

        verify(authorizationService, never()).checkUserAuthorization(any());
    }

    @Test
    void updateWithExistedProduct() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }

    @Test
    void destroyWithNotExistedProduct() throws Exception {
        mockMvc.perform(
                delete("/products/1000")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isNotFound());

        verify(productService).deleteProduct(1000L);
    }
}
