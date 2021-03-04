package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    private static final Long EXIST_ID = 1L;
    private static final Long NOT_EXIST_ID = 100L;

    private static final String NAME = "뱀 장난감";
    private static final String MAKER = "야옹이네 장난감";
    private static final Integer PRICE = 3000;
    private static final String IMAGE = "https://bit.ly/3qzXRME";

    private static final String UPDATE_NAME = "물고기 장난감";
    private static final String UPDATE_MAKER = "애옹이네 장난감";
    private static final Integer UPDATE_PRICE = 5000;
    private static final String UPDATE_IMAGE = "https://bit.ly/2M4YXkw";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyIElEIjoxfQ.K7brB9EalPWQiDi8EF6XriOiJiKgksnJxIujpPVY";

    private Product product;
    private Product updated;
    private Product invalidAttributes;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();

        product = Product.builder()
                .name(NAME)
                .maker(MAKER)
                .price(PRICE)
                .image(IMAGE)
                .build();

        updated = Product.builder()
                .name(UPDATE_NAME)
                .maker(UPDATE_MAKER)
                .price(UPDATE_PRICE)
                .image(UPDATE_IMAGE)
                .build();

        invalidAttributes = Product.builder()
                .name("")
                .maker("")
                .price(0)
                .image("")
                .build();

        products.add(product);

        given(productService.getProducts()).willReturn(products);

        given(productService.getProduct(EXIST_ID)).willReturn(product);

        given(productService.getProduct(NOT_EXIST_ID))
                .willThrow(new ProductNotFoundException(NOT_EXIST_ID));

        given(productService.createProduct(any(ProductData.class)))
                .willReturn(product);

        given(productService.updateProduct(eq(EXIST_ID), any(ProductData.class)))
                .willReturn(updated);

        given(productService.updateProduct(eq(NOT_EXIST_ID), any(ProductData.class)))
                .willThrow(new ProductNotFoundException(NOT_EXIST_ID));

        given(productService.deleteProduct(NOT_EXIST_ID))
                .willThrow(new ProductNotFoundException(NOT_EXIST_ID));

        given(authenticationService.parseToken(VALID_TOKEN)).willReturn(1L);

        given(authenticationService.parseToken(INVALID_TOKEN))
                .willThrow(new InvalidTokenException(INVALID_TOKEN));
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("야옹이네 장난감")));

        verify(productService).getProducts();
    }

    @Test
    void detailWithValidId() throws Exception {
        mockMvc.perform(get("/products/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("뱀 장난감")));

        verify(productService).getProduct(EXIST_ID);
    }

    @Test
    void detailWithInvalidId() throws Exception {
        mockMvc.perform(get("/products/100"))
                .andExpect(status().isNotFound());

        verify(productService).getProduct(NOT_EXIST_ID);
    }

    @Test
    void createWithValidAccessToken() throws Exception {
        mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(NAME))
                .andExpect(jsonPath("maker").value(MAKER))
                .andExpect(jsonPath("price").value(PRICE))
                .andExpect(jsonPath("image").value(IMAGE));

        verify(productService).createProduct(any(ProductData.class));
    }

    @Test
    void createWithInvalidAccessToken() throws Exception {
        mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createWithoutAccessToken() throws Exception {
        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createWithValidAttributes() throws Exception {
        mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(NAME))
                .andExpect(jsonPath("maker").value(MAKER))
                .andExpect(jsonPath("price").value(PRICE))
                .andExpect(jsonPath("image").value(IMAGE));

        verify(productService).createProduct(any(ProductData.class));

    }

    @Test
    void createWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAttributes))
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithExistProduct() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(UPDATE_NAME))
                .andExpect(jsonPath("maker").value(UPDATE_MAKER))
                .andExpect(jsonPath("price").value(UPDATE_PRICE))
                .andExpect(jsonPath("image").value(UPDATE_IMAGE));

        verify(productService).updateProduct(eq(EXIST_ID), any(ProductData.class));
    }

    @Test
    void updateWithNotExistedProduct() throws Exception {
        mockMvc.perform(
                patch("/products/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated))
        )
                .andExpect(status().isNotFound());

        verify(productService).updateProduct(eq(NOT_EXIST_ID), any(ProductData.class));
    }

    @Test
    void updateWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAttributes))
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteWithExistedProduct() throws Exception {
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk());

        verify(productService).deleteProduct(EXIST_ID);
    }

    @Test
    void deleteWithNotExistedProduct() throws Exception {
        mockMvc.perform(delete("/products/100"))
                .andExpect(status().isNotFound());

        verify(productService).deleteProduct(NOT_EXIST_ID);
    }
}
