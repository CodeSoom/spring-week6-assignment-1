package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.controller.ControllerTest;
import com.codesoom.assignment.domain.products.Product;
import com.codesoom.assignment.domain.products.ProductRepository;
import com.codesoom.assignment.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("ProductDeleteController 클래스")
public class ProductDeleteControllerMockMvcTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;
    private static final String TOKEN_PREFIX = "Bearer ";
    private String TOKEN;

    @BeforeEach
    void setup() {
        this.TOKEN = jwtUtil.encode(1L);
    }

    @DisplayName("delete 메서드는")
    @Nested
    class Describe_delete {

        @DisplayName("찾을 수 있는 상품의 id가 주어지면")
        @Nested
        class Context_with_exist_id {

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                final Product product
                        = new Product("쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "");
                this.EXIST_ID = repository.save(product).getId();
            }

            @DisplayName("해당 상품을 삭제한다.")
            @Test
            void it_delete_product() throws Exception {
                mockMvc.perform(delete("/products/" + EXIST_ID)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + TOKEN))
                        .andExpect(status().isNoContent());
            }
        }

        @DisplayName("찾을 수 없는 상품의 id가 주어지면")
        @Nested
        class Context_with_not_exist_id {

            private final Long NOT_EXIST_ID = 100L;

            @BeforeEach
            void setup() {
                if (repository.existsById(NOT_EXIST_ID)) {
                    repository.deleteById(NOT_EXIST_ID);
                }
            }

            @DisplayName("404 not found를 응답한다.")
            @Test
            void will_response_404_not_found() throws Exception {
                mockMvc.perform(delete("/products/" + NOT_EXIST_ID)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + TOKEN))
                        .andExpect(status().isNotFound());
            }
        }

        @DisplayName("유효하지 않은 토큰이 주어지면")
        @Nested
        class Context_with_invalid_token {

            private String[] INVALID_TOKENS = {
                    ""
                    , " "
                    , TOKEN + "231"
            };

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                final Product product
                        = Product.withoutId("쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "");
                this.EXIST_ID = repository.save(product).getId();
            }

            @DisplayName("401 unauthorized를 응답한다.")
            @Test
            void it_delete_product() throws Exception {
                for (int i = 0; i < INVALID_TOKENS.length; i++) {
                    mockMvc.perform(delete("/products/" + EXIST_ID)
                                    .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + INVALID_TOKENS[i]))
                            .andExpect(status().isUnauthorized());
                }
            }
        }
    }
}
