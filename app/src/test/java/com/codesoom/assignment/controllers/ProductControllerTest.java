package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String ACCESS_INVALID_TOKEN = ACCESS_TOKEN + "invalid";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    AuthenticationService authenticationService;

    private ProductData productData;
    private Product product;

    @BeforeEach
    void setUp() {

        productData = ProductData.builder()
                .name("name1")
                .maker("maker1")
                .price(1000L)
                .imageUrl("img1").build();

        product = Product.builder()
                .name("name1")
                .maker("maker1")
                .price(1000L)
                .imageUrl("img1")
                .build();

        given(productService.createProduct(any(ProductData.class))).willReturn(any(Product.class));
        given(authenticationService.parseToken(ACCESS_TOKEN)).willReturn(1L);
        given(authenticationService.parseToken(null)).willThrow(UnauthorizedException.class);
        given(authenticationService.parseToken(ACCESS_INVALID_TOKEN)).willThrow(UnauthorizedException.class);

    }

    @Test
    void createWithAccessToken() throws Exception {

        mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productData))
                                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                )
                .andExpect(status().isCreated());

    }

    @Test
    void createWithInvalidAccessToken() throws Exception {

        mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productData))
                                .header("Authorization", "Bearer " + ACCESS_INVALID_TOKEN)
                )
                .andExpect(status().isUnauthorized());

    }

    @Test
    void createWithOutAccessToken() throws Exception {

        mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productData))
                )
                .andExpect(status().isUnauthorized());

    }

}

