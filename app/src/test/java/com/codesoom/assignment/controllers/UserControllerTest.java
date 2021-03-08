package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserCreateRequestDto;
import com.codesoom.assignment.dto.UserUpdateRequestDto;
import com.codesoom.assignment.errors.UserNotFoundException;
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
    private User updatedUser;
    private UserCreateRequestDto createRequest;
    private UserUpdateRequestDto updateRequest;
    private Long givenValidId;
    private Long givenInvalidId;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        updatedUser = User.builder()
                .name(NAME)
                .email(UPDATE_EMAIL)
                .password(UPDATE_PASSWORD)
                .build();

        given(userService.createUser(any(UserCreateRequestDto.class)))
                .willReturn(user);

        given(userService.updateUser(eq(EXIST_ID), any(UserUpdateRequestDto.class)))
                .willReturn(updatedUser);

        given(userService.updateUser(eq(NOT_EXIST_ID), any(UserUpdateRequestDto.class)))
                .willThrow(new UserNotFoundException(NOT_EXIST_ID));

        given(userService.deleteUser(NOT_EXIST_ID))
                .willThrow(new UserNotFoundException(NOT_EXIST_ID));
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        UserCreateRequestDto invalidAttributes;

        @Nested
        @DisplayName("사용자 정보가 주어진다면")
        class Context_with_valid_attributes {

            @BeforeEach
            void setUp() {
                createRequest = UserCreateRequestDto.builder()
                        .name(NAME)
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("새로운 사용자를 등록하고 사용자와 응답코드 201을 반환한다")
            void it_returns_user_and_created() throws Exception {
                mockMvc.perform(
                        post("/users")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(createRequest))
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
                invalidAttributes = UserCreateRequestDto.builder()
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
        UserUpdateRequestDto invalidAttributes;

        @Nested
        @DisplayName("등록된 사용자 ID와 수정할 정보가 주어진다면")
        class Context_with_existed_id_and_valid_attributes {

            @BeforeEach
            void setUp() {
                updateRequest = UserUpdateRequestDto.builder()
                        .email(UPDATE_EMAIL)
                        .password(UPDATE_PASSWORD)
                        .build();

                givenValidId = EXIST_ID;
            }

            @Test
            @DisplayName("해당 ID를 갖는 사용자의 정보를 수정하고 사용자와 응답코드 200을 반환한다")
            void it_returns_user_and_ok() throws Exception {
                mockMvc.perform(
                        patch("/users/1")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("email").value(UPDATE_EMAIL))
                        .andExpect(jsonPath("password").value(UPDATE_PASSWORD));

                verify(userService).updateUser(eq(givenValidId), any(UserUpdateRequestDto.class));
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

                givenInvalidId = NOT_EXIST_ID;
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

                verify(userService).updateUser(eq(givenInvalidId), any(UserUpdateRequestDto.class));
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

            @BeforeEach
            void setUp() {
                givenValidId = EXIST_ID;
            }

            @Test
            @DisplayName("해당 ID를 갖는 사용자를 삭제하고 응답코드 200을 반환한다")
            void it_returns_ok() throws Exception {
                mockMvc.perform(delete("/users/1"))
                        .andExpect(status().isOk());

                verify(userService).deleteUser(givenValidId);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 사용자 ID가 주어진다면")
        class Context_with_not_existed_id {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;
            }

            @Test
            @DisplayName("응답코드 404를 반환한다")
            void it_returns_not_found() throws Exception {
                mockMvc.perform(delete("/users/100"))
                        .andExpect(status().isNotFound());

                verify(userService).deleteUser(givenInvalidId);
            }
        }
    }
}
