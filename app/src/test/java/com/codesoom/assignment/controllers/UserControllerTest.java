package com.codesoom.assignment.controllers;

import com.codesoom.assignment.UserNotFoundException;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserCreateRequestDto;
import com.codesoom.assignment.dto.UserUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private static final Long EXIST_ID = 1L;
    private static final Long NOT_EXIST_ID = 100L;

    private static final String NAME = "양효주";
    private static final String EMAIL = "yhyojoo@codesoom.com";
    private static final String PASSWORD = "112233!!";

    private static final String UPDATE_EMAIL = "joo@codesoom.com";
    private static final String UPDATE_PASSWORD = "123!";

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        User invalidAttributes;

        @Nested
        @DisplayName("사용자 정보가 주어진다면")
        class Context_with_valid_attributes {

            @BeforeEach
            void setUp() {
                given(userService.createUser(any(UserCreateRequestDto.class)))
                        .willReturn(user);
            }

            @Test
            @DisplayName("새로운 사용자를 등록하고 사용자와 응답코드 201을 반환한다")
            void it_returns_user_and_created() throws Exception {
                mockMvc.perform(
                        post("/users")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(user))
                )
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("name").value(NAME))
                        .andExpect(jsonPath("email").value(EMAIL))
                        .andExpect(jsonPath("password").value(PASSWORD));

                verify(userService).createUser(any(UserCreateRequestDto.class));
            }
        }

        @Nested
        @DisplayName("사용자 정보가 주어지지 않는다면")
        class Context_with_invalid_attributes {

            @BeforeEach
            void setUp() {
                invalidAttributes = User.builder()
                        .name("")
                        .email("")
                        .password("")
                        .build();
            }

            @Test
            @DisplayName("응답코드 400을 반환한다")
            void it_returns_bad_request() throws Exception {
                mockMvc.perform(
                        post("/users")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(invalidAttributes))
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        UserUpdateRequestDto updateRequest;
        UserUpdateRequestDto invalidAttributes;
        User updatedUser;

        @Nested
        @DisplayName("등록된 사용자 ID와 수정할 정보가 주어진다면")
        class Context_with_existed_id_and_valid_attributes {

            @BeforeEach
            void setUp() {
                updateRequest = UserUpdateRequestDto.builder()
                        .email(UPDATE_EMAIL)
                        .password(UPDATE_PASSWORD)
                        .build();

                updatedUser = userService.updateUser(EXIST_ID, updateRequest);

                given(userService.updateUser(eq(EXIST_ID), any(UserUpdateRequestDto.class)))
                        .willReturn(updatedUser);

            }

            @Test
            @DisplayName("해당 ID를 갖는 사용자의 정보를 수정하고 사용자와 응답코드 200을 반환한다")
            void it_returns_user_and_ok() throws Exception {
                mockMvc.perform(
                        patch("/products/1")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedUser))
                )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("email").value(UPDATE_EMAIL))
                        .andExpect(jsonPath("password").value(UPDATE_PASSWORD));

                verify(userService).updateUser(eq(EXIST_ID), any(UserUpdateRequestDto.class));
            }
        }

        @Nested
        @DisplayName("등록되지 않은 사용자 ID와 수정할 정보가 주어진다면")
        class Context_with_not_existed_id_and_valid_attributes {

            @BeforeEach
            void setUp() {
                updateRequest = UserUpdateRequestDto.builder()
                        .email(UPDATE_EMAIL)
                        .password(UPDATE_PASSWORD)
                        .build();

                given(userService.updateUser(eq(NOT_EXIST_ID), any(UserUpdateRequestDto.class)))
                        .willThrow(new UserNotFoundException(NOT_EXIST_ID));
            }

            @Test
            @DisplayName("응답코드 404를 반환한다")
            void it_returns_not_found() throws Exception {
                mockMvc.perform(
                        patch("/users/100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                        .andExpect(status().isNotFound());

                verify(userService).updateUser(eq(NOT_EXIST_ID), any(UserUpdateRequestDto.class));
            }
        }

        @Nested
        @DisplayName("수정할 사용자 정보가 주어지지 않는다면")
        class Context_with_invalid_attributes {

            @BeforeEach
            void setUp() {
                invalidAttributes = UserUpdateRequestDto.builder()
                        .email("")
                        .password("")
                        .build();
            }

            @Test
            @DisplayName("응답코드 400을 반환한다")
            void it_returns_bad_request() throws Exception {
                mockMvc.perform(
                        patch("/users/1")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidAttributes))
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("등록된 사용자 ID가 주어진다면")
        class Context_with_existed_id {

            @Test
            @DisplayName("해당 ID를 갖는 사용자를 삭제하고 응답코드 200을 반환한다")
            void it_returns_ok() throws Exception {
                mockMvc.perform(delete("/users/1"))
                        .andExpect(status().isOk());

                verify(userService).deleteUser(EXIST_ID);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 사용자 ID가 주어진다면")
        class Context_with_not_existed_id {

            @BeforeEach
            void setUp() {
                given(userService.deleteUser(NOT_EXIST_ID))
                        .willThrow(new UserNotFoundException(NOT_EXIST_ID));
            }

            @Test
            @DisplayName("응답코드 404를 반환한다")
            void it_returns_not_found() throws Exception {
                mockMvc.perform(delete("/users/100"))
                        .andExpect(status().isNotFound());

                verify(userService).deleteUser(NOT_EXIST_ID);
            }
        }
    }

}
