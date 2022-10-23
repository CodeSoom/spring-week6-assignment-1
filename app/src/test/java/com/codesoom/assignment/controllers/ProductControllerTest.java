package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.exception.ProductNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    private static final Product PRODUCT = Product.builder()
            .id(1L)
            .name("쥐돌이")
            .maker("냥이월드")
            .price(5000)
            .build();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void list() throws Exception {
        given(productService.getProducts()).willReturn(List.of(PRODUCT));

        mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")));
    }

    @Test
    void detailWithExistProduct() throws Exception {
        given(productService.getProduct(1L)).willReturn(PRODUCT);

        mockMvc.perform(get("/products/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")));
    }

    @Test
    void detailWithNotExistProduct() throws Exception {
        given(productService.getProduct(1000L))
                .willThrow(ProductNotFoundException.class);

        mockMvc.perform(get("/products/1000"))
                .andExpect(status().isNotFound());
    }
}
