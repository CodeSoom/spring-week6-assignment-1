package com.codesoom.assignment.controllers;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("ProductController 테스트")
class ProductControllerTest {
    private static final String PRODUCT_NAME = "장난감 뱀";
    private static final String PRODUCT_MAKER = "애용이네 장난감";
    private final Integer PRODUCT_PRICE = 5000;

    private Product existedProduct;
    private ProductData productData;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Mapper mapper;

    void prepareProduct() {
        productData = ProductData.builder()
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
    class Describe_list {
        @Nested
        @DisplayName("상품이 하나도 없다면")
        class Context_not_existed_product {
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
                        .andExpect(content().string(containsString(EMPTY_PRODUCTS)));
            }
        }

        @Nested
        @DisplayName("상품이 있다면")
        class Context_existed_product {

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

        @Nested
        @DisplayName("존재하는 id로 요청하면")
        class Context_existed_id {

            @BeforeEach
            void prepare() {
                prepareProduct();
            }

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
        class Context_not_existed_id {

            private Product notExistedProduct;

            @BeforeEach
            void prepare() {
                prepareProduct();
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
}
