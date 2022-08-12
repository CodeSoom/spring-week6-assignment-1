package com.codesoom.assignment.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {
    public static final String USER_PATH = "/users";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public static final String EMAIL = "qjawlsqjacks@naver.com";
    public static final String NAME = "BJP";
    public static final String PASSWORD = "1234";
    private static final Map<String, String> REGISTER_DATA = Map.of(
            "email", EMAIL,
            "name", NAME,
            "password", PASSWORD
    );

    private Map<String, String> createUser(Map<String, String> registerData) throws Exception {
        return objectMapper.readValue(
                mockMvc.perform(post("users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerData)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), new TypeReference<>() {}
        );
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {
        @Nested
        @DisplayName("유저 정보가 주어지면")
        class Context_with_userData {
            @Test
            @DisplayName("유저를 생성하고 유저와 201 을 응답한다")
            void It_respond_createdUser() throws Exception {
                mockMvc.perform(post(USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(REGISTER_DATA)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.email", Is.is(EMAIL)))
                        .andExpect(jsonPath("$.name", Is.is(NAME)));
            }
        }
    }
}
