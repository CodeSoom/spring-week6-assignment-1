package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("ProductController 테스트")
class ProductControllerTest {
    private static final String PRODUCT_NAME = "장난감 뱀";
    private static final String PRODUCT_MAKER = "애용이네 장난감";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9._sqAnLqnuii5tTri0u8AAwGJpI4PF6WRT9wkOLyxWaw";
    private static final String INVALID_TOKEN = VALID_TOKEN + "0";
    private final Integer PRODUCT_PRICE = 5000;
    private Product existedProduct;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Mapper mapper;

    @MockBean
    private AuthenticationService authenticationService;

    void prepareProduct() {
        ProductData productData = ProductData.builder()
                .name(PRODUCT_NAME)
                .maker(PRODUCT_MAKER)
                .price(PRODUCT_PRICE)
                .build();

        existedProduct = mapper.map(productData, Product.class);
        productRepository.save(existedProduct);
    }

    @BeforeEach
    void setUpMockMvc() {
        this.mapper = DozerBeanMapperBuilder.buildDefault();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Nested
    @DisplayName("GET /products 는")
    class Describe_get_products {

        @Nested
        @DisplayName("상품이 하나도 없다면")
        class Context_with_not_existed_product {
            private static final String EMPTY_PRODUCTS = "[]";

            @BeforeEach
            void prepare() {
                productRepository.deleteAll();
            }

            @Test
            @DisplayName("빈 배열을 응답한다.")
            void it_response_empty_array() throws Exception {
                mockMvc.perform(get("/products"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(equalTo(EMPTY_PRODUCTS)));
            }
        }

        @Nested
        @DisplayName("상품이 있다면")
        class Context_with_existed_product {

            @BeforeEach
            void prepare() {
                prepareProduct();
            }

            @Test
            @DisplayName("전체 상품을 응답한다.")
            void it_response_products() throws Exception {
                mockMvc.perform(get("/products"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(PRODUCT_NAME)))
                        .andExpect(content().string(containsString(PRODUCT_MAKER)));
            }
        }
    }

    @Nested
    @DisplayName("GET /products/{id} 요청은")
    class Describe_get_products_detail {

        @BeforeEach
        void prepare() {
            prepareProduct();
        }

        @Nested
        @DisplayName("존재하는 id로 요청하면")
        class Context_with_existed_id {

            @Test
            @DisplayName("해당 id의 상품을 응답한다")
            void it_response_detail_product() throws Exception {
                mockMvc.perform(get("/products/" + existedProduct.getId()))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").value(existedProduct.getId()))
                        .andExpect(jsonPath("name").value(PRODUCT_NAME))
                        .andExpect(jsonPath("maker").value(PRODUCT_MAKER))
                        .andExpect(jsonPath("price").value(PRODUCT_PRICE));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id로 요청하면")
        class Context_with_not_existed_id {

            private Product notExistedProduct;

            @BeforeEach
            void prepare() {
                productRepository.delete(existedProduct);
                notExistedProduct = existedProduct;
            }

            @Test
            @DisplayName("NotFound를 응답한다.")
            void it_response_not_found() throws Exception {
                mockMvc.perform(get("/products/" + notExistedProduct.getId()))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("POST /products 요청은")
    class Describe_post_products_request {

        private Product newProduct;
        private String requestContent;

        @Nested
        @DisplayName("유효한 access token이 있고, 상품 정보가 주어진다면")
        class Context_with_new_product_data {
            private final static String NEW_PRODUCT_NAME = "도마뱀인형";
            private final static String NEW_PRODUCT_MAKER = "코드숨";
            private final static String NEW_PRODUCT_IMAGE_URL = "someUrl";

            @BeforeEach
            void prepare() throws JsonProcessingException {
                given(authenticationService.parseToken(VALID_TOKEN)).willReturn(1L);

                Integer NEW_PRODUCT_PRICE = 10000;
                ProductData productData = ProductData.builder()
                        .name(NEW_PRODUCT_NAME)
                        .maker(NEW_PRODUCT_MAKER)
                        .imageUrl(NEW_PRODUCT_IMAGE_URL)
                        .price(NEW_PRODUCT_PRICE)
                        .build();

                newProduct = mapper.map(productData, Product.class);
                requestContent = objectMapper.writeValueAsString(newProduct);
            }

            @Test
            @DisplayName("상품을 추가하고, 추가된 상품을 응답한다.")
            void it_response_new_product() throws Exception {
                mockMvc.perform(
                                post("/products")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestContent)
                                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        )
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("name").value(NEW_PRODUCT_NAME))
                        .andExpect(jsonPath("maker").value(NEW_PRODUCT_MAKER));
            }
        }

        @Nested
        @DisplayName("유효한 access token이 있지만, 상품 정보가 비어있다면")
        class Context_with_blank_product_data {
            private final static String BLANK_PRODUCT_NAME = "";
            private final static String BLANK_PRODUCT_MAKER = "";
            private final static String NEW_PRODUCT_IMAGE_URL = "someUrl";

            @BeforeEach
            void prepare() throws JsonProcessingException {
                given(authenticationService.parseToken(VALID_TOKEN)).willReturn(1L);

                Integer NEW_PRODUCT_PRICE = 5000;
                ProductData productData = ProductData.builder()
                        .name(BLANK_PRODUCT_NAME)
                        .maker(BLANK_PRODUCT_MAKER)
                        .imageUrl(NEW_PRODUCT_IMAGE_URL)
                        .price(NEW_PRODUCT_PRICE)
                        .build();

                newProduct = mapper.map(productData, Product.class);
                requestContent = objectMapper.writeValueAsString(newProduct);
            }

            @Test
            @DisplayName("Bad request를 응답한다.")
            void it_response_bad_request() throws Exception {
                mockMvc.perform(
                                post("/products")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestContent)
                                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        )
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("access token이 비어 있다면")
        class Context_without_access_token {
            private final static String NEW_PRODUCT_NAME = "도마뱀인형";
            private final static String NEW_PRODUCT_MAKER = "코드숨";
            private final static String NEW_PRODUCT_IMAGE_URL = "someUrl";

            @BeforeEach
            void prepare() throws JsonProcessingException {
                Integer NEW_PRODUCT_PRICE = 5000;
                ProductData productData = ProductData.builder()
                        .name(NEW_PRODUCT_NAME)
                        .maker(NEW_PRODUCT_MAKER)
                        .imageUrl(NEW_PRODUCT_IMAGE_URL)
                        .price(NEW_PRODUCT_PRICE)
                        .build();

                newProduct = mapper.map(productData, Product.class);
                requestContent = objectMapper.writeValueAsString(newProduct);
            }

            @Test
            @DisplayName("Unauthorized를 응답한다")
            void it_response_unauthorized() throws Exception {
                mockMvc.perform(
                                post("/products")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestContent)
                        )
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 access token이 주어진다면")
        class Context_wrong_access_token {
            private final static String NEW_PRODUCT_NAME = "도마뱀인형";
            private final static String NEW_PRODUCT_MAKER = "코드숨";
            private final static String NEW_PRODUCT_IMAGE_URL = "someUrl";

            @BeforeEach
            void prepare() throws JsonProcessingException {
                given(authenticationService.parseToken(INVALID_TOKEN))
                        .willThrow(new InvalidTokenException(INVALID_TOKEN));

                Integer NEW_PRODUCT_PRICE = 5000;
                ProductData productData = ProductData.builder()
                        .name(NEW_PRODUCT_NAME)
                        .maker(NEW_PRODUCT_MAKER)
                        .imageUrl(NEW_PRODUCT_IMAGE_URL)
                        .price(NEW_PRODUCT_PRICE)
                        .build();

                newProduct = mapper.map(productData, Product.class);
                requestContent = objectMapper.writeValueAsString(newProduct);
            }

            @Test
            @DisplayName("Unauthorized를 응답한다")
            void it_response_unauthorized() throws Exception {
                mockMvc.perform(
                                post("/products")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestContent)
                                        .header("Authorization", "Bearer " + INVALID_TOKEN)
                        )
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("PATCH /products/{id} 요청은")
    class Describe_patch_products_request {
        private final static String UPDATE_PRODUCT_NAME = "코끼리인형";
        private final static String UPDATE_PRODUCT_MAKER = "컬리";

        private Product updatedProduct;
        private String requestContent;
        private Product notExistedProduct;

        @BeforeEach
        void prepare() throws JsonProcessingException {
            prepareProduct();

            Integer UPDATE_PRODUCT_PRICE = 10000;
            ProductData productData = ProductData.builder()
                    .name(UPDATE_PRODUCT_NAME)
                    .maker(UPDATE_PRODUCT_MAKER)
                    .price(UPDATE_PRODUCT_PRICE)
                    .build();

            updatedProduct = mapper.map(productData, Product.class);
            requestContent = objectMapper.writeValueAsString(updatedProduct);
        }

        @Nested
        @DisplayName("존재하는 id이고, 수정할 상품 데이터가 주어지면")
        class Context_with_existed_id_and_product_data {

            @Test
            @DisplayName("수정된 상품 정보를 응답한다.")
            void it_response_updated_product_data() throws Exception {
                mockMvc.perform(
                                patch("/products/" + existedProduct.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestContent)
                        )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("name").value(UPDATE_PRODUCT_NAME));
            }
        }

        @Nested
        @DisplayName("존재하는 id이고, 수정할 상품 데이터가 비어있다면")
        class Context_with_existed_id_and_blank_product_data {

            @BeforeEach
            void prepare() throws JsonProcessingException {
                Integer UPDATE_PRODUCT_PRICE = 10000;
                ProductData productData = ProductData.builder()
                        .name("")
                        .maker("")
                        .price(UPDATE_PRODUCT_PRICE)
                        .build();

                updatedProduct = mapper.map(productData, Product.class);
                requestContent = objectMapper.writeValueAsString(updatedProduct);
            }

            @Test
            @DisplayName("Bad request를 응답한다.")
            void it_response_bad_request() throws Exception {
                mockMvc.perform(
                                patch("/products/" + existedProduct.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestContent)
                        )
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id이면")
        class Context_with_not_existed_id {

            @BeforeEach
            void prepare() {
                productRepository.delete(existedProduct);
                notExistedProduct = existedProduct;
            }

            @Test
            @DisplayName("Not found를 응답한다.")
            void it_response_not_found() throws Exception {
                mockMvc.perform(
                                patch("/products/" + notExistedProduct.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestContent)
                        )
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("DELETE /products/{id} 요청은")
    class Describe_delete_product_request {

        @BeforeEach
        void prepare() {
            prepareProduct();
        }

        @Nested
        @DisplayName("존재하는 상품 id라면")
        class Context_with_existed_product_id {

            @Test
            @DisplayName("해당 id의 상품을 삭제한다.")
            void it_destroy_product() throws Exception {
                mockMvc.perform(delete("/products/" + existedProduct.getId()))
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id라면")
        class Context_with_not_existed_product_id {

            private Product notExistedProduct;

            @BeforeEach
            void prepare() {
                productRepository.delete(existedProduct);
                notExistedProduct = existedProduct;
            }

            @Test
            @DisplayName("Not found를 응답한다.")
            void it_response_not_found() throws Exception {
                mockMvc.perform(delete("/products/" + notExistedProduct.getId()))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
