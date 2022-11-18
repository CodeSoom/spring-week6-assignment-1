package com.codesoom.assignment.common.auth;

import com.codesoom.assignment.common.util.JsonUtil;
import com.codesoom.assignment.domain.product.domain.Product;
import com.codesoom.assignment.domain.product.presentation.ProductController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.codesoom.assignment.support.ProductFixture.TOY_1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationInterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductController productController;

    @Nested
    @DisplayName("Auth 토큰이 존재하지 않을 경우")
    class Context_with_not_exist_auth_token {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 장난감_등록하기_API는 {
            @Test
            @DisplayName("401 코드로 응답한다")
            void it_responses_401() throws Exception {
                mockMvc.perform(
                                post("/products")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(JsonUtil.writeValue(TOY_1.요청_데이터_생성()))
                        )
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 장난감_수정하기_API는 {
            private Long fixtureId;

            @BeforeEach
            void setUpCreateFixture() {
                Product productSource = productController.create(TOY_1.요청_데이터_생성());
                fixtureId = productSource.getId();
            }

            @Test
            @DisplayName("401 코드로 응답한다")
            void it_responses_401() throws Exception {
                mockMvc.perform(
                                patch("/products/" + fixtureId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(JsonUtil.writeValue(TOY_1.요청_데이터_생성()))
                        )
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 장난감_삭제하기_API는 {
            private Long fixtureId;

            @BeforeEach
            void setUpCreateFixture() {
                Product productSource = productController.create(TOY_1.요청_데이터_생성());
                fixtureId = productSource.getId();
            }

            @Test
            @DisplayName("401 코드로 응답한다")
            void it_responses_401() throws Exception {
                mockMvc.perform(
                                delete("/products/" + fixtureId)
                        )
                        .andExpect(status().isUnauthorized());
            }
        }
    }
}
