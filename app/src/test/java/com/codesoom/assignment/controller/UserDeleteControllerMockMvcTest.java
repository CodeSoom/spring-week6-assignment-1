package com.codesoom.assignment.controller;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("UserDeleteController 클래스")
public class UserDeleteControllerMockMvcTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @DisplayName("[DELETE] /users/{id}")
    @Nested
    class Describe_delete_user {

        @DisplayName("찾을 수 있는 회원의 id가 주어지면")
        @Nested
        class Context_with_exist_user {

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                this.EXIST_ID = repository
                        .save(new User("홍길동", "hgd@codesoom.com", "hgd123!"))
                        .getId();
            }

            @DisplayName("성공적으로 회원을 삭제한다.")
            @Test
            void it_will_delete_user() throws Exception {
                mockMvc.perform(delete("/users/" + EXIST_ID))
                        .andExpect(status().isNoContent());

                assertThat(repository.findById(EXIST_ID)).isEmpty();
            }
        }

        @DisplayName("찾을 수 없는 회원의 id가 주어지면")
        @Nested
        class Context_with_not_exist_user {

            private final Long NOT_EXIST_ID = 999L;

            @BeforeEach
            void setup() {
                if (repository.existsById(NOT_EXIST_ID)) {
                    repository.deleteById(NOT_EXIST_ID);
                }
            }

            @DisplayName("404 not found를 응답한다.")
            @Test
            void it_response_not_found() throws Exception {
                mockMvc.perform(delete("/users/" + NOT_EXIST_ID))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
