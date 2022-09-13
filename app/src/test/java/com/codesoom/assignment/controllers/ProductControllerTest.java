package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(ProductController.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOTESTjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private final String CONTENT = "{\"name\":\"쥐돌이\",\"maker\":\"코드숨\",\"price\":1000}";

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

    }

    @Nested
    @DisplayName("create()")
    class Describe_Create{

        @Nested
        @DisplayName("인증 헤더가 존재하지 않는다면")
        class Context_NotExistedAuthenticationHeader{

            @Test
            @DisplayName("권한에 대한 승인이 거부되었다는 응답 코드를 반환한다.")
            void It_() throws Exception {
                mockMvc.perform(
                                post("/products")
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(CONTENT)
                        )
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("인증 헤더의 토큰이 유효하지 않거나 공백이라면")
        class Context_NullOrBlankAuthenticationHeader{

            @Test
            @DisplayName("401 응답 코드를 반환하고 유효하지 않은 토큰이라는 예외를 던진다.")
            void It_ThrowException() throws Exception {
                mockMvc.perform(
                                post("/products")
                                        .header("Authorization" ," ")
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(CONTENT)
                        )
                        .andExpect(status().isUnauthorized());

                mockMvc.perform(
                                post("/products")
                                        .header("Authorization" ,"Bearer " + INVALID_TOKEN)
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(CONTENT)
                        )
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("정상적인 토큰이 담겨있다면")
        class Context_InvalidToken{

            @Test
            @DisplayName("상품을 저장한다.")
            void It_SaveProduct() throws Exception {
                mockMvc.perform(
                                post("/products")
                                        .header("Authorization" , "Bearer " + VALID_TOKEN)
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(CONTENT)
                        )
                        .andExpect(status().isUnauthorized());
            }
        }
    }
}
