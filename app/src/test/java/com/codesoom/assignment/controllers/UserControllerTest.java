package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.errors.UserEmailDuplicationException;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@DisplayName("UserController 테스트")
class UserControllerTest {
    private static final Long ID = 1L;
    private static final String EMAIL = "tester@example.com";
    private static final String NAME = "Tester";
    private static final String PASSWORD = "test12#$";

    private static final Long NOT_REGISTED_ID = 9999L;
    private static final String DUPLICATED_EMAIL = EMAIL + "Duplicated";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserRegistrationData userRegistrationData;

    @BeforeEach
    void setUp() {
        userRegistrationData = UserRegistrationData.builder()
                .email(EMAIL)
                .name(NAME)
                .password(PASSWORD)
                .build();

        given(userService.registerUser(any(UserRegistrationData.class)))
                .will(invocation -> {
                    UserRegistrationData registrationData = invocation.getArgument(0);

                    if (registrationData.getEmail().equals(DUPLICATED_EMAIL)) {
                        throw new UserEmailDuplicationException(DUPLICATED_EMAIL);
                    }

                    return User.builder()
                            .id(ID)
                            .email(registrationData.getEmail())
                            .name(registrationData.getName())
                            .build();
                });

        given(userService.updateUser(eq(ID), any(UserModificationData.class)))
                .will(invocation -> {
                    Long id = invocation.getArgument(0);
                    UserModificationData modificationData =
                            invocation.getArgument(1);
                    return User.builder()
                            .id(id)
                            .email(userRegistrationData.getEmail())
                            .name(modificationData.getName())
                            .password(modificationData.getPassword())
                            .build();
                });

        given(userService.updateUser(eq(NOT_REGISTED_ID), any(UserModificationData.class)))
                .willThrow(new UserNotFoundException(NOT_REGISTED_ID));

        given(userService.deleteUser(eq(NOT_REGISTED_ID)))
                .willThrow(new UserNotFoundException(NOT_REGISTED_ID));
    }

    @Nested
    @DisplayName("POST /users")
    class DescribeRegisterUser {
        @Nested
        @DisplayName("요청 데이터가 유효한 경우")
        class ContextWithValidAttributes {
            @Nested
            @DisplayName("신규 이메일이라면")
            class ContextWithNotExistedEmail {
                @Test
                @DisplayName("등록한 회원 정보를 반환한다")
                void itReturnsCreatedWithRegistedUser() throws Exception {
                    mockMvc.perform(
                            post("/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            String.format(
                                                    "{\"email\":\"%s\"," +
                                                            "\"name\":\"%s\"," +
                                                            "\"password\":\"%s\"}",
                                                    EMAIL, NAME, PASSWORD
                                            )
                                    )
                    )
                            .andExpect(status().isCreated())
                            .andExpect(content().string(
                                    containsString("\"id\":" + ID)
                            ))
                            .andExpect(content().string(
                                    containsString("\"email\":\"" + EMAIL + "\"")
                            ))
                            .andExpect(content().string(
                                    containsString("\"name\":\"" + NAME + "\"")
                            ));
                }
            }

            @Nested
            @DisplayName("이미 등록된 이메일이라면")
            class ContextWithExistedEmail {
                @Test
                @DisplayName("BAD REQUEST를 응답한다")
                void itReturnsCreatedWithRegistedUser() throws Exception {
                    mockMvc.perform(
                            post("/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            String.format(
                                                    "{\"email\":\"%s\"," +
                                                            "\"name\":\"%s\"," +
                                                            "\"password\":\"%s\"}",
                                                    DUPLICATED_EMAIL , NAME, PASSWORD
                                            )
                                    )
                    )
                            .andExpect(status().isBadRequest());
                }
            }

        }

        @Nested
        @DisplayName("요청 데이터가 잘못된 경우")
        class ContextWithInvalidAttributes {
            @Test
            @DisplayName("BAD REQUEST를 응답한다")
            void registerUserWithInvalidAttributes() throws Exception {
                mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("PATCH /users/:id")
    class DescribeUpdateUser {
        @Nested
        @DisplayName("요청 데이터가 유효한 경우")
        class ContextWithValidAttributes {
            @Nested
            @DisplayName("존재하는 식별자인 경우")
            class ContextWithExistedId {
                @Test
                @DisplayName("갱신한 회원 정보를 반환한다")
                void updateUserWithValidAttributes() throws Exception {
                    mockMvc.perform(
                            patch("/users/" + ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\"name\":\"TEST\",\"password\":\"test\"}")
                    )
                            .andExpect(status().isOk())
                            .andExpect(content().string(
                                    containsString("\"id\":" + ID)
                            ))
                            .andExpect(content().string(
                                    containsString("\"email\":\"" + EMAIL + "\"")
                            ))
                            .andExpect(content().string(
                                    containsString("\"name\":\"TEST\"")
                            ))
                            .andExpect(content().string(
                                    containsString("\"name\":\"TEST\"")
                            ));
                }
            }

            @Nested
            @DisplayName("존재하지 않는 식별자인 경우")
            class ContextWithNotExistedId {
                @Test
                @DisplayName("NOT FOUND를 응답한다")
                void updateUserWithNotExsitedId() throws Exception {
                    mockMvc.perform(
                            patch("/users/" + NOT_REGISTED_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\"name\":\"TEST\",\"password\":\"TEST\"}")
                    )
                            .andExpect(status().isNotFound());
                }
            }
        }

        @Nested
        @DisplayName("요청 데이터가 잘못된 경우")
        class ContextWithInvalidAttributes {
            @Test
            @DisplayName("BAD REQUEST를 응답한다")
            void updateUserWithInvalidAttributes() throws Exception {
                mockMvc.perform(
                        patch("/users/" + ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"\",\"password\":\"\"}")
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("DELETE /users/:id")
    class DescribeDeleteUser {
        @Nested
        @DisplayName("존재하는 식별자인 경우")
        class ContextWithExistedId {
            @Test
            @DisplayName("NO CONTENT를 응답한다")
            void destroyWithExistedId() throws Exception {
                mockMvc.perform(delete("/users/" + ID))
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 식별자인 경우")
        class ContextWithNotExistedId {
            @Test
            @DisplayName("NOT FOUND를 응답한다")
            void itReturnsNotFound() throws Exception {
                mockMvc.perform(delete("/users/" + NOT_REGISTED_ID))
                        .andExpect(status().isNotFound());

                verify(userService).deleteUser(NOT_REGISTED_ID);
            }
        }
    }
}
