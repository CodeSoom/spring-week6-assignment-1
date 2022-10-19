package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.request.ProductData;
import com.codesoom.assignment.dto.request.UserRegistrationData;
import com.codesoom.assignment.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("ProductController 권한 테스트")
public class ProductControllerAuthTest {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "t!@#$%";
    private static final UserRegistrationData USER = UserRegistrationData.builder()
            .email(EMAIL)
            .password(PASSWORD)
            .build();
    private static final ProductData PRODUCT = ProductData.builder()
            .name("test product")
            .maker("test maker")
            .price(10000)
            .build();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void clear() {
        userRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("회원 정보를 찾을 수 있는 토큰이 주어지면")
        class Context_with_valid_token {
            private String accessToken;

            @BeforeEach
            void setUp() {
                User user = userService.registerUser(USER);
                accessToken = jwtUtil.createToken(user.getId());
            }

            @Test
            @DisplayName("생성된 상품 정보와 201 응답을 생성한다")
            void it_responds_created() throws Exception {
                mockMvc.perform(post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION_HEADER, BEARER + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(PRODUCT)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").isNotEmpty())
                        .andExpect(jsonPath("$.name").value(PRODUCT.getName()))
                        .andExpect(jsonPath("$.maker").value(PRODUCT.getMaker()))
                        .andExpect(jsonPath("$.price").value(PRODUCT.getPrice()));
            }
        }

        @Nested
        @DisplayName("회원 정보를 찾을 수 없는 토큰이 주어지면")
        class Context_with_invalid_token {
            private String invalidToken;

            @BeforeEach
            void setUp() {
                User user = userService.registerUser(USER);
                invalidToken = jwtUtil.createToken(user.getId());
                userService.deleteUser(user.getId());
            }

            @Test
            @DisplayName("404 응답을 생성한다")
            void it_responds_not_found() throws Exception {
                mockMvc.perform(post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION_HEADER, BEARER + invalidToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(PRODUCT)))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("빈 토큰이 주어지면")
        class Context_with_empty_token {

            @Test
            @DisplayName("401 응답을 생성한다")
            void it_responds_unAuthorized() throws Exception {
                mockMvc.perform(post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION_HEADER, BEARER + "")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(PRODUCT)))
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("토큰이 주어지지 않으면")
        class Context_without_token {

            @Test
            @DisplayName("401 응답을 생성한다")
            void it_responds_unauthorized() throws Exception {
                mockMvc.perform(post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(PRODUCT)))
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("회원 정보를 찾을 수 있는 토큰이 주어지면")
        class Context_with_valid_token {
            private String accessToken;
            private Long productId;
            private final Product updatedProduct = Product.builder()
                    .name("update product")
                    .maker("update maker")
                    .price(15000)
                    .build();

            @BeforeEach
            void setUp() {
                User user = userService.registerUser(USER);
                accessToken = jwtUtil.createToken(user.getId());
                Product product = productService.createProduct(PRODUCT);
                productId = product.getId();
            }

            @Test
            @DisplayName("수정된 상품 정보와 200 응답을 생성한다")
            void it_responds_ok() throws Exception {
                mockMvc.perform(patch("/products/{id}", productId)
                                .accept(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION_HEADER, BEARER + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedProduct)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(productId))
                        .andExpect(jsonPath("$.name").value(updatedProduct.getName()))
                        .andExpect(jsonPath("$.maker").value(updatedProduct.getMaker()))
                        .andExpect(jsonPath("$.price").value(updatedProduct.getPrice()));
            }
        }

        @Nested
        @DisplayName("회원 정보를 찾을 수 없는 토큰이 주어지면")
        class Context_with_invalid_token {
            private String invalidToken;
            private Long productId;
            private final Product updatedProduct = Product.builder()
                    .name("update product")
                    .maker("update maker")
                    .price(10000)
                    .build();

            @BeforeEach
            void setUp() {
                User user = userService.registerUser(USER);
                invalidToken = jwtUtil.createToken(user.getId());

                userService.deleteUser(user.getId());

                Product product = productService.createProduct(PRODUCT);
                productId = product.getId();
            }

            @Test
            @DisplayName("404 응답을 생성한다")
            void it_responds_not_found() throws Exception {
                mockMvc.perform(patch("/products/{id}", productId)
                                .accept(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION_HEADER, BEARER + invalidToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedProduct)))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("빈 토큰이 주어지면")
        class Context_with_empty_token {
            private Long productId;

            @BeforeEach
            void setUp() {
                Product product = productService.createProduct(PRODUCT);
                productId = product.getId();
            }

            @Test
            @DisplayName("401 응답을 생성한다")
            void it_responds_unAuthorized() throws Exception {
                mockMvc.perform(patch("/products/{id}", productId)
                                .accept(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION_HEADER, BEARER + "")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(PRODUCT)))
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("토큰이 주어지지 않으면")
        class Context_without_token {
            private Long productId;

            @BeforeEach
            void setUp() {
                Product product = productService.createProduct(PRODUCT);
                productId = product.getId();
            }

            @Test
            @DisplayName("401 응답을 생성한다")
            void it_responds_unauthorized() throws Exception {
                mockMvc.perform(patch("/products/{id}", productId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(PRODUCT)))
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    class Describe_destroy {
        // TODO
    }
}
