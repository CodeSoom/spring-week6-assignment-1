package com.codesoom.assignment.controller;

import com.codesoom.assignment.AutoConfigureUtf8MockMvc;
import com.codesoom.assignment.application.product.ProductCommandServiceTestDouble;
import com.codesoom.assignment.application.product.ProductQueryServiceTestDouble;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureUtf8MockMvc
@DisplayName("ProductController MockMVC 에서")
class ProductControllerWebTest {
    private static final String PRODUCT_NAME = "상품1";
    private static final String PRODUCT_MAKER = "메이커1";
    private static final Integer PRODUCT_PRICE = 100000;
    private static final String PRODUCT_IMAGE_URL = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F9941A1385B99240D2E";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductQueryServiceTestDouble productQueryServiceTestDouble;
    @Autowired
    private ProductCommandServiceTestDouble productCommandServiceTestDouble;

    @Nested
    @DisplayName("GET - /products")
    class Describe_of_GET {

        @Nested
        @DisplayName("조회할 수 있는 Product가 있을 경우")
        class Content_with_list {

            @BeforeEach
            void setUp() {
                ProductDto productDto = ProductDto.builder()
                        .name(PRODUCT_NAME)
                        .price(PRODUCT_PRICE)
                        .maker(PRODUCT_MAKER)
                        .imageUrl(PRODUCT_IMAGE_URL)
                        .build();
                productCommandServiceTestDouble.create(productDto);
            }

            @Test
            @DisplayName("모든 Prouct를 보여준다")
            void it_return_all_products() throws Exception {
                List<Product> products = productQueryServiceTestDouble.products();
                System.out.println(products);
                mockMvc.perform(get("/products"))
                        .andExpect(status().isOk())
                        .andExpect(content()
                                .string(containsString(
                                        objectMapper.writeValueAsString(products)
                                )));
            }
        }
    }

}