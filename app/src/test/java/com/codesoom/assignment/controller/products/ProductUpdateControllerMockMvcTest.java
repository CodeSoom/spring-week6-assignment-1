package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.controller.ControllerTest;
import com.codesoom.assignment.domain.products.Product;
import com.codesoom.assignment.domain.products.ProductDto;
import com.codesoom.assignment.domain.products.ProductRepository;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.domain.users.UserSaveDto;
import com.codesoom.assignment.dto.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("ProductUpdateController 클래스")
public class ProductUpdateControllerMockMvcTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TOKEN_PREFIX = "Bearer ";
    private String TOKEN;

    @BeforeEach
    void setup() throws Exception {
        cleanup();
        this.TOKEN = createToken();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
        userRepository.deleteAll();
    }

    String createToken() throws Exception {
        final String email = "hgd@codesoom.com";
        final String password = "hgd123098!#";
        userRepository.save(new UserSaveDto("홍길동", email, password));

        final byte[] contentAsByteArray = mockMvc.perform(post("/session")
                .content(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsByteArray();
        final TokenResponse tokenResponse = objectMapper.readValue(contentAsByteArray, TokenResponse.class);
        return TOKEN_PREFIX + tokenResponse.getAccessToken();
    }

    @DisplayName("updateProduct 메서드는")
    @Nested
    class Describe_update_product {

        private final ProductDto productToUpdate
                = new ProductDto("소쩍새", "유령회사", BigDecimal.valueOf(3000), "");

        @DisplayName("찾을 수 있는 상품의 id가 주어지면")
        @Nested
        class Context_with_exist_id {
            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                this.EXIST_ID = repository.save(Product.withoutId(
                        "쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "")).getId();
            }

            @DisplayName("수정된 상품을 반환한다.")
            @Test
            void will_return_updated_product() throws Exception {
                final MvcResult result = mockMvc.perform(patch("/products/" + EXIST_ID)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .content(objectMapper.writeValueAsString(productToUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

                final Product product
                        = objectMapper.readValue(result.getResponse().getContentAsByteArray(), Product.class);
                assertThat(product.getName()).isEqualTo(productToUpdate.getName());
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
            void will_return_updated_product() throws Exception {
                mockMvc.perform(patch("/products/" + NOT_EXIST_ID)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .content(objectMapper.writeValueAsString(productToUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }

        @DisplayName("필수값을 모두 입력하면")
        @Nested
        class Context_with_valid_data {

            private final ProductDto VALID_PRODUCT_DTO
                    = new ProductDto("어쩌구", "어쩌구컴퍼니", BigDecimal.valueOf(2000), "url");

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                this.EXIST_ID = repository.save(Product.withoutId(
                        "쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "")).getId();
            }

            @AfterEach
            void cleanup() {
                repository.deleteAll();
            }

            @DisplayName("상품을 성공적으로 수정한다.")
            @Test
            void it_will_save_product() throws Exception {
                mockMvc.perform(patch("/products/" + EXIST_ID).accept(MediaType.APPLICATION_JSON_UTF8)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .content(objectMapper.writeValueAsString(VALID_PRODUCT_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(VALID_PRODUCT_DTO.getName())));
            }
        }

        @DisplayName("필수값을 하나라도 입력하지 않으면")
        @Nested
        class Context_with_invalid_data {

            private final ProductDto INVALID_PRODUCT_DTO
                    = new ProductDto("어쩌구", " ", BigDecimal.valueOf(2000), "url");

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                this.EXIST_ID = repository.save(Product.withoutId(
                        "쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "")).getId();
            }

            @DisplayName("400 bad request를 응답한다.")
            @Test
            void it_thrown_exception() throws Exception {
                mockMvc.perform(patch("/products/" + EXIST_ID)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .content(objectMapper.writeValueAsString(INVALID_PRODUCT_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }

        @DisplayName("유효하지 않은 토큰이 주어지면")
        @Nested
        class Context_with_invalid_token {

            private final String[] INVALID_TOKENS = {
                    TOKEN_PREFIX + ""
                    , TOKEN_PREFIX + " "
                    , TOKEN_PREFIX + "esldkjflsoeis"
                    };

            private final ProductDto VALID_PRODUCT_DTO
                    = new ProductDto("어쩌구", "어쩌구컴퍼니", BigDecimal.valueOf(2000), "url");

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                this.EXIST_ID = repository.save(Product.withoutId(
                        "쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "")).getId();
            }

            @DisplayName("401 unauthorized를 응답한다.")
            @Test
            void it_thrown_exception() throws Exception {
                for (int i = 0; i < INVALID_TOKENS.length; i++) {
                    mockMvc.perform(patch("/products/" + EXIST_ID)
                                    .header(HttpHeaders.AUTHORIZATION, INVALID_TOKENS[i])
                                    .content(objectMapper.writeValueAsString(VALID_PRODUCT_DTO))
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isUnauthorized());
                }
            }
        }
    }

}
