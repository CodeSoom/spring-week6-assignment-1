package com.codesoom.assignment.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InFqYXdsc3FqYWNrc0BuYXZlci5jb20ifQ.Kp42APjRQt9BsUDief7z63Oz257gC7fbh47zyWsPrjo";
    private static final String INVALID_TOKEN = VALID_TOKEN + "0";
    public static final String VALID_NAME = "mouse";
    public static final String VALID_MAKER = "codesoom";
    public static final int VALID_PRICE = 99999;
    public static final Map<String, Object> GIVEN_PRODUCT = Map.of(
            "name", VALID_NAME,
            "maker", VALID_MAKER,
            "price", VALID_PRICE
    );
    public static final String CHANGE_PREFIX = "변경";
    public static final Map<String, Object> GIVEN_PRODUCT_TO_CHANGE = Map.of(
            "name", CHANGE_PREFIX + VALID_NAME,
            "maker", CHANGE_PREFIX + VALID_MAKER,
            "price", VALID_PRICE + 1
    );
    public static final String PRODUCT_PATH = "/products";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Object> createProduct(Map<String, Object> product, String token) throws Exception {
        return objectMapper.readValue(mockMvc.perform(post(PRODUCT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .header("Authorization", token))
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeReference<>() {
        });
    }

    @ParameterizedTest(name = "token = ''{0}''")
    @ValueSource(strings = {"", " ", "Bearer ", INVALID_TOKEN})
    @DisplayName("상품을 생성할 때, 유효하지 않은 토큰이 주어지면 에러 메시지와 상태코드 401을 응답한다.")
    void whenCreatingProductAuthenticatedTokenRequired(String token) throws Exception {
        mockMvc.perform(post(PRODUCT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(GIVEN_PRODUCT))
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").isString());
    }

    @Test
    @DisplayName("유효한 토큰과 상품 정보가 주어지면 상품을 생성하고 응답한다")
    void returnAndCreateWithValidTokenAndProductData() throws Exception {
        mockMvc.perform(post(PRODUCT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(GIVEN_PRODUCT))
                        .header("Authorization", VALID_TOKEN)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Is.is(VALID_NAME)))
                .andExpect(jsonPath("$.maker", Is.is(VALID_MAKER)))
                .andExpect(jsonPath("$.price", Is.is(VALID_PRICE)));
    }

    @Test
    @DisplayName("update 메서드는 유효한 토큰과 변경할 상품 정보가 주어지면 상품 정보를 수정하고 응답한다")
    void returnAndUpdateProductWithData() throws Exception {
        // Given
        Map<String, Object> product = createProduct(GIVEN_PRODUCT, VALID_TOKEN);
        Object productId = product.get("id");

        // When
        ResultActions changedProduct = mockMvc.perform(put(PRODUCT_PATH + "/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(GIVEN_PRODUCT_TO_CHANGE))
                .header("Authorization", VALID_TOKEN));

        // Then
        changedProduct
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Is.is(CHANGE_PREFIX + VALID_NAME)))
                .andExpect(jsonPath("$.maker", Is.is(CHANGE_PREFIX + VALID_MAKER)))
                .andExpect(jsonPath("$.price", Is.is(VALID_PRICE + 1)));
    }

    @Test
    @DisplayName("update 메서드는 유효하지 않은 토큰과 변경할 상품 정보가 주어지면 예외 메시지를 응답한다")
    void returnErrorMessageWhenUpdateGivenInvalidToken() throws Exception {
        // Given
        Map<String, Object> product = createProduct(GIVEN_PRODUCT, VALID_TOKEN);
        Object productId = product.get("id");

        // When
        ResultActions changedProduct = mockMvc.perform(put(PRODUCT_PATH + "/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(GIVEN_PRODUCT_TO_CHANGE))
                .header("Authorization", INVALID_TOKEN));

        // Then
        changedProduct
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").isString());
    }

    @Test
    @DisplayName("delete 메서드는 유효한 토큰이 주어지면 상품을 삭제하고 204를 응답한다")
    void returnNoContentWhenDeleteGivenValidToken() throws Exception {
        // Given
        Map<String, Object> createdProduct = createProduct(GIVEN_PRODUCT, VALID_TOKEN);
        Object productId = createdProduct.get("id");

        // When
        ResultActions response = mockMvc.perform(delete(PRODUCT_PATH + "/" + productId)
                .header("Authorization", VALID_TOKEN));

        // Then
        response.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("delete 메서드는 유효하지 않은 토큰이 주어지면 예외 메시지를 응답한다")
    void returnErrorMessageWhenDeleteGivenInvalidToken() throws Exception {
        // Given
        Map<String, Object> createdProduct = createProduct(GIVEN_PRODUCT, VALID_TOKEN);
        Object productId = createdProduct.get("id");

        // When
        ResultActions response = mockMvc.perform(delete(PRODUCT_PATH + "/" + productId)
                .header("Authorization", INVALID_TOKEN));

        // Then
        response.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("list 메서드는 상품 목록이 주어지면 상품 목록과 200을 응답한다")
    void returnListWhenListGivenProducts() throws Exception {
        // Given
        createProduct(GIVEN_PRODUCT, VALID_TOKEN);
        createProduct(GIVEN_PRODUCT, VALID_TOKEN);
        createProduct(GIVEN_PRODUCT, VALID_TOKEN);
        createProduct(GIVEN_PRODUCT, INVALID_TOKEN);

        // When
        ResultActions response = mockMvc.perform(get(PRODUCT_PATH));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DisplayName("detail 메서드는 상품이 주어지면 상품과 200을 응답한다")
    void returnProductWhenDetailGivenProduct() throws Exception {
        // Given
        Map<String, Object> givenProduct = createProduct(GIVEN_PRODUCT, VALID_TOKEN);
        Object findId = givenProduct.get("id");

        // When
        ResultActions response = mockMvc.perform(get(PRODUCT_PATH + "/" + findId));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(findId)))
                .andExpect(jsonPath("$.name", Is.is(givenProduct.get("name"))))
                .andExpect(jsonPath("$.maker", Is.is(givenProduct.get("maker"))))
                .andExpect(jsonPath("$.price", Is.is(givenProduct.get("price"))))
                .andExpect(jsonPath("$.imageUrl", Is.is(givenProduct.get("imageUrl"))));
    }

    @Test
    @DisplayName("detail 메서드는 상품을 찾지 못하면 예외 메시지를 응답한다")
    void returnErrorMessageWhenDetailNotGivenProduct() throws Exception {
        mockMvc.perform(get(PRODUCT_PATH + "/" + -1))
                .andExpect(status().isNotFound());
    }
}
