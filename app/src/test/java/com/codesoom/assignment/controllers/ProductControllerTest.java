package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ProductData productDataFixture;
    private ProductData updatedProductDataFixture;
    private ProductData invalidProductDataFixture;
    private String authorizationFixture;

    private <T> T getResponseContent(ResultActions actions, TypeReference<T> type)
            throws UnsupportedEncodingException, JsonProcessingException {
        MvcResult mvcResult = actions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        return objectMapper.readValue(contentAsString, type);
    }

    private Product createProductBeforeTest(ProductData productData) throws Exception {
        ResultActions actions = mockMvc.perform(post("/products")
                .content(objectMapper.writeValueAsString(productData))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorizationFixture)
        );

        Product createdProduct = getResponseContent(actions, new TypeReference<Product>() {});

        return createdProduct;
    }

    private void deleteProductBeforeTest(Long id) throws Exception {
        mockMvc.perform(delete("/products/" + id)
                .header("Authorization", authorizationFixture));
    }

    @BeforeEach
    void mockParseValidToken() {
        given(authService.parseToken(any(String.class))).willReturn(1L);
    }

    @BeforeEach
    void setupFixtures() {
        productDataFixture = ProductData.builder()
                .name("mouse")
                .maker("adidas")
                .price(5000)
                .build();

        updatedProductDataFixture = ProductData.builder()
                .name("mouse2")
                .maker("new balance")
                .price(5000)
                .build();

        invalidProductDataFixture = ProductData.builder()
                .name("mouse")
                .build();

        authorizationFixture = "Bearer 111.222.333";
    }

    @Nested
    @DisplayName("Post Request")
    class PostRequest {
        @DisplayName("responses with a created product and http status code 201")
        @Test
        void responsesWithCreatedProduct() throws Exception {
            mockMvc.perform(post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(productDataFixture))
                            .header("Authorization", authorizationFixture)
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString(productDataFixture.getName())));
        }

        @Nested
        @DisplayName("Without a token")
        class WithoutToken {
            @DisplayName("responses with http status code 401")
            @Test
            void responsesWithUnauthorizedError() throws Exception {
                mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDataFixture))
                        )
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("With an invalid request body")
        class WithInvalidRequestBody {
            @DisplayName("responses with http status code 400")
            @Test
            void responsesWithBadRequest() throws Exception {
                mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidProductDataFixture))
                                .header("Authorization", authorizationFixture)
                        )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("Get Request")
    class GetRequest {
        Product product;

        @BeforeEach
        void setupProduct() throws Exception {
            product = createProductBeforeTest(productDataFixture);
        }

        @Nested
        @DisplayName("Without a path parameter")
        class WithoutPathParameter {
            @Test
            @DisplayName("responses with products and http status code 200")
            void responsesWithProducts() throws Exception {
                mockMvc.perform(get("/products"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(product.getName())));
            }
        }

        @Nested
        @DisplayName("With an existent id")
        class WithExistentId {
            @Test
            @DisplayName("responses with a product and http status code 200")
            void responsesWithProduct() throws Exception {
                mockMvc.perform(get("/products/" + product.getId()))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(product.getName())));
            }
        }

        @Nested
        @DisplayName("With a non existent id")
        class WithNonExistentId {
            @BeforeEach
            void removeProduct() throws Exception {
                deleteProductBeforeTest(product.getId());
            }

            @Test
            @DisplayName("responses with http status code 404")
            void responsesWithNotFoundError() throws Exception {
                mockMvc.perform(get("/products/"+ product.getId()))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("Patch Request")
    class PatchRequest {
        Product product;

        @BeforeEach
        void setupProduct() throws Exception {
            product = createProductBeforeTest(productDataFixture);
        }

        @DisplayName("responses with an updated product and http status code 200")
        @Test
        void responsesWithUpdatedProduct() throws Exception {
            mockMvc.perform(patch("/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProductDataFixture))
                        .header("Authorization", authorizationFixture))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(updatedProductDataFixture.getName())));
        }

        @Nested
        @DisplayName("Without a token")
        class WithoutToken {
            @DisplayName("responses with http status code 401")
            @Test
            void responsesWithUnauthorizedError() throws Exception {
                mockMvc.perform(patch("/products/" + product.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedProductDataFixture)))
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("With an invalid request body")
        class WithInvalidRequestBody {
            @DisplayName("responses with http status code 400")
            @Test
            void responsesWithBadRequest() throws Exception {
                mockMvc.perform(patch("/products/" + product.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidProductDataFixture))
                                .header("Authorization", authorizationFixture))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("With a non existent id")
        class WithNonExistentId {
            @BeforeEach
            void removeProduct() throws Exception {
                deleteProductBeforeTest(product.getId());
            }

            @DisplayName("responses with http status code 404")
            @Test
            void responsesWithNotFoundError() throws Exception {
                mockMvc.perform(patch("/products" + product.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedProductDataFixture))
                                .header("Authorization", authorizationFixture))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("Delete Request")
    class DeleteRequest {
        Product product;

        @BeforeEach
        void setupProduct() throws Exception {
            product = createProductBeforeTest(productDataFixture);
        }

        @Test
        @DisplayName("responses with a http status code 204")
        void responsesWithNoContentStatus() throws Exception {
            mockMvc.perform(delete("/products/" + product.getId())
                            .header("Authorization", authorizationFixture))
                .andExpect(status().isNoContent());
        }

        @Nested
        @DisplayName("Without a token")
        class WithoutToken {
            @Test
            @DisplayName("responses with a http status code 401")
            void responsesWithUnauthorizedError() throws Exception {
                mockMvc.perform(delete("/products/" + product.getId()))
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("With a non existent id")
        class WithNonExistentId {
            @BeforeEach
            void removeProduct() throws Exception {
                deleteProductBeforeTest(product.getId());
            }

            @Test
            @DisplayName("responses with a http status code 404")
            void responsesWithNotFoundError() throws Exception {
                mockMvc.perform(delete("/products/" + product.getId())
                                .header("Authorization", authorizationFixture))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
