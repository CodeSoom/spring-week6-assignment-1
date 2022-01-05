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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ProductControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Mapper mapper;

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

        private Product product;
        private ProductData productData;

        @Nested
        @DisplayName("상품이 있다면")
        class Context_existed_product {
            private static final String PRODUCT_NAME = "장난감 뱀";
            private static final String PRODUCT_MAKER = "애용이네 장난감";

            @BeforeEach
            void setUp() {
                productData = ProductData.builder()
                        .id(1L)
                        .name(PRODUCT_NAME)
                        .maker(PRODUCT_MAKER)
                        .price(5000)
                        .build();

                product = mapper.map(productData, Product.class);
                productRepository.save(product);
            }

            @Test
            @DisplayName("전체 상품을 리턴한다.")
            void it_return_products() throws Exception {
                mockMvc.perform(get("/products"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(PRODUCT_NAME)))
                        .andExpect(content().string(containsString(PRODUCT_MAKER)));
            }
        }
    }
}
