package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.controller.ControllerTest;
import com.codesoom.assignment.domain.products.ProductDto;
import com.codesoom.assignment.domain.products.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("ProductSaveController 클래스")
public class ProductSaveControllerMockMvcTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @DisplayName("saveProduct 메서드는")
    @Nested
    class Describe_save_product {

        @DisplayName("필수값을 모두 입력하면")
        @Nested
        class Context_with_valid_data {

            private final ProductDto VALID_PRODUCT_DTO
                    = new ProductDto("어쩌구", "어쩌구컴퍼니", BigDecimal.valueOf(2000), "url");

            @AfterEach
            void cleanup() {
                repository.deleteAll();
            }

            @DisplayName("상품을 성공적으로 등록한다.")
            @Test
            void it_will_save_product() throws Exception {
                mockMvc.perform(post("/products").accept(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(VALID_PRODUCT_DTO))
                        .contentType(MediaType.APPLICATION_JSON ))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(VALID_PRODUCT_DTO.getName())));
            }
        }

        @DisplayName("필수값을 하나라도 입력하지 않으면")
        @Nested
        class Context_with_invalid_data {

            private final ProductDto INVALID_PRODUCT_DTO
                    = new ProductDto("어쩌구", " ", BigDecimal.valueOf(2000), "url");

            @DisplayName("400 bad request를 응답한다.")
            @Test
            void it_reponse_400_bad_request() throws Exception {
                mockMvc.perform(post("/products")
                        .content(objectMapper.writeValueAsString(INVALID_PRODUCT_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }
    }

}
