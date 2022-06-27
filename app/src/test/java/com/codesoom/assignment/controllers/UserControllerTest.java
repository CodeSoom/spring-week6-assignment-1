package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.errors.BadRequestException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * UserController에 대한 테스트 클래스
 */
@WebMvcTest(UserController.class)
class UserControllerTest {
    private static final Long ID = 1L;
    private static final Long NON_EXISTING_ID = 1000L;
    private static final String NAME = "tester";
    private static final String INVALID_NAME = "";
    private static final String NEW_NAME = "tester2";
    private static final String EMAIL = "tester@example.com";
    private static final String PASSWORD = "test";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;
    private UserRegistrationData userRegistrationData;
    private UserModificationData userModificationData;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        userRegistrationData = UserRegistrationData.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        userModificationData = UserModificationData.builder()
                .name(NEW_NAME)
                .password(PASSWORD)
                .build();
    }


    @Nested
    @DisplayName("POST /users 요청 시")
    class Describe_post_users {
        @Nested
        @DisplayName("모든 속성이 유효한 user가 주어졌을 경우")
        class Context_if_user_with_valid_attributes_given {
            @BeforeEach
            void setUp() {
                given(userService.registerUser(any(UserRegistrationData.class)))
                        .will(invocation -> {
                            UserRegistrationData registrationData = invocation.getArgument(0);

                            return User.builder()
                                    .id(ID)
                                    .email(registrationData.getEmail())
                                    .name(registrationData.getName())
                                    .build();
                        });
            }

            @Nested
            @DisplayName("status code 201을 응답한다")
            class It_response_status_code_201 {
                ResultActions subject() throws Exception {
                    return mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(userRegistrationData))
                    );
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isCreated())
                            .andExpect(jsonPath("$.id").value(ID))
                            .andExpect(jsonPath("$.name").value(NAME))
                            .andExpect(jsonPath("$.email").value(EMAIL));

                    verify(userService).registerUser(any(UserRegistrationData.class));
                }
            }
        }

        @Nested
        @DisplayName("유효하지 않은 이름을 가진 user가 주어졌을 경우")
        class Context_if_user_with_invalid_name_given {
            @BeforeEach
            void setUp() {
                userRegistrationData = UserRegistrationData.builder()
                        .name(INVALID_NAME)
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build();

                given(userService.registerUser(eq(userRegistrationData)))
                        .willThrow(new BadRequestException());
            }

            @Nested
            @DisplayName("status code 400을 응답한다")
            class It_response_status_code_400 {
                ResultActions subject() throws Exception {
                    return mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(userRegistrationData))
                    );
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isBadRequest());
                }
            }
        }
    }

    @Nested
    @DisplayName("PATCH /users/{id} 요청 시")
    class Describe_patch_users_by_id {
        @Nested
        @DisplayName("존재하는 id가 주어졌을 경우")
        class Context_if_existing_id_given {
            @BeforeEach
            void setUp() {
                given(userService.updateUser(eq(ID), any(UserModificationData.class)))
                        .will(invocation -> {
                            Long id = invocation.getArgument(0);
                            UserModificationData userModificationData = invocation.getArgument(1);

                            return User.builder()
                                    .id(id)
                                    .name(userModificationData.getName())
                                    .password(userModificationData.getPassword())
                                    .build();
                        });
            }

            @Nested
            @DisplayName("status code 200을 응답한다")
            class It_response_status_code_200{
                ResultActions subject() throws Exception {
                    return mockMvc.perform(patch("/users/{id}", ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(userModificationData))
                    );
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isOk())
                            .andExpect(jsonPath("$.name").value(NEW_NAME));

                    verify(userService).updateUser(eq(1L), any(UserModificationData.class));
                }
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어졌을 경우")
        class Context_if_non_existing_id_given {
            @BeforeEach
            void setUp() {
                given(userService.updateUser(eq(NON_EXISTING_ID), any(UserModificationData.class)))
                        .willThrow(new UserNotFoundException(NON_EXISTING_ID));
            }

            @Nested
            @DisplayName("status code 404를 응답한다")
            class It_response_status_code_404 {
                ResultActions subject() throws Exception {
                    return mockMvc.perform(patch("/users/{id}", NON_EXISTING_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(userModificationData))
                    );
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isNotFound());

                    verify(userService).updateUser(eq(NON_EXISTING_ID), any(UserModificationData.class));
                }
            }
        }

        @Nested
        @DisplayName("유효하지 않은 이름을 가진 user가 주어졌을 경우")
        class Context_if_user_with_invalid_name_given {
            @BeforeEach
            void setUp() {
                userModificationData = UserModificationData.builder()
                        .name(INVALID_NAME)
                        .password(PASSWORD)
                        .build();

                given(userService.updateUser(eq(ID), eq(userModificationData)))
                        .willThrow(new BadRequestException());
            }

            @Nested
            @DisplayName("status code 400을 응답한다")
            class It_response_status_code_400 {
                ResultActions subject() throws Exception {
                    return mockMvc.perform(patch("/users/{id}", ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(userModificationData))
                    );
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isBadRequest());
                }
            }
        }
    }

    @Nested
    @DisplayName("DELETE /users 요청 시")
    class Describe_delete_users_by_id {
        @Nested
        @DisplayName("존재하는 id가 주어졌을 경우")
        class Context_if_existing_id_given {
            @Nested
            @DisplayName("status code 204를 응답한다")
            class It_response_status_code_204{
                ResultActions subject() throws Exception {
                    return mockMvc.perform(delete("/users/{id}", ID));
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isNoContent());

                    verify(userService).deleteUser(ID);
                }
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어졌을 경우")
        class Context_if_non_existing_id_given {
            @BeforeEach
            void setUp() {
                given(userService.deleteUser(NON_EXISTING_ID)).willThrow(new UserNotFoundException(NON_EXISTING_ID));
            }

            @Nested
            @DisplayName("status code 404를 응답한다")
            class It_response_status_code_404 {
                ResultActions subject() throws Exception {
                    return mockMvc.perform(delete("/users/{id}", NON_EXISTING_ID));
                }

                @Test
                void test() throws Exception {
                    subject().andExpect(status().isNotFound());

                    verify(userService).deleteUser(NON_EXISTING_ID);
                }
            }
        }
    }

    private String toJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(object);
    }
}
