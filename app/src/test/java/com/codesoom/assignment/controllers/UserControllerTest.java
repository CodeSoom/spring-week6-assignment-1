package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserCreateData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.dto.UserUpdateData;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
@DisplayName("UserController 테스트")
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    private WebApplicationContext wac;

    private final String CREATE_USER_NAME = "createdName";
    private final String CREATE_USER_EMAIL = "createdEmail";
    private final String CREATE_USER_PASSWORD = "createdPassword";

    private final String UPDATE_USER_NAME = "updatedName";
    private final String UPDATE_USER_EMAIL = "updatedEmail";
    private final String UPDATE_USER_PASSWORD = "updatedPassword";

    private final Mapper mapper = DozerBeanMapperBuilder.buildDefault();
    private final Long EXISTED_ID = 1L;
    private final Long NOT_EXISTED_ID = 100L;

    private List<User> users;
    private User setUpUser;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(wac).addFilter(((request, response, chain) -> {
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        })).build();

        setUpUser = User.builder()
                        .id(EXISTED_ID)
                        .name(CREATE_USER_NAME)
                        .email(CREATE_USER_EMAIL)
                        .password(CREATE_USER_PASSWORD)
                        .build();

        users = List.of(setUpUser);
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {
        @Nested
        @DisplayName("만약 사용자가 주어진다면")
        class Context_WithUser {
            private UserCreateData userCreateData;
            private UserResultData userResultData;

            @BeforeEach
            void setUp() {
                userCreateData = UserCreateData.builder()
                        .name(CREATE_USER_NAME)
                        .email(CREATE_USER_EMAIL)
                        .password(CREATE_USER_PASSWORD)
                        .build();

                userResultData = mapper.map(userCreateData, UserResultData.class);
            }

            @Test
            @DisplayName("사용자를 저장하고 저장된 사용자와 CREATED를 리턴한다")
            void itSavesUserAndReturnsUser() throws Exception {
                given(userService.createUser(any(UserCreateData.class)))
                        .will(invocation -> {
                           UserCreateData userCreateData = invocation.getArgument(0);
                            return UserResultData.builder()
                                    .id(EXISTED_ID)
                                    .name(userCreateData.getName())
                                    .email(userCreateData.getEmail())
                                    .password(userCreateData.getPassword())
                                    .build();
                        });

                mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"createdName\",\"email\":\"createdEmail\",\"password\":\"createdPassword\"}"))
                        .andExpect(content().string(containsString("\"id\":" + EXISTED_ID)))
                        .andExpect(jsonPath("name").value(userResultData.getName()))
                        .andExpect(jsonPath("email").value(userResultData.getEmail()))
                        .andExpect(jsonPath("password").value(userResultData.getPassword()))
                        .andExpect(status().isCreated());

                verify(userService).createUser(any(UserCreateData.class));
            }
        }

        @Nested
        @DisplayName("만약 비어있는 값이 주어진다면")
        class Context_WithEmpty {
            @Test
            @DisplayName("BAD_REQUEST를 리턴한다")
            void itReturnsBAD_REQUESTHttpStatus() throws Exception {
                mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {
        @Nested
        @DisplayName("만약 저장되어 있는 사용자의 아이디와 수정 할 사용자가 주어진다면")
        class Context_WithExistedIdAndUser {
            private final Long givenExistedId = EXISTED_ID;
            private UserUpdateData userUpdateData;

            @BeforeEach
            void setUp() {
                userUpdateData = UserUpdateData.builder()
                        .name(UPDATE_USER_NAME)
                        .password(UPDATE_USER_PASSWORD)
                        .build();

                userUpdateData = mapper.map(userUpdateData, UserUpdateData.class);
            }

            @Test
            @DisplayName("주어진 아이디에 해당하는 사용자를 수정하고 수정된 사용자와 OK를 리턴한다")
            void itUpdatesUserAndReturnUpdatedUserAndOKHttpStatus() throws Exception {
                given(userService.updateUser(eq(givenExistedId), any(UserUpdateData.class)))
                        .will(invocation -> {
                            Long id = invocation.getArgument(0);
                            UserUpdateData userUpdateData = invocation.getArgument(1);
                            return UserResultData.builder()
                                    .id(id)
                                    .name(userUpdateData.getName())
                                    .password(userUpdateData.getPassword())
                                    .build();
                        });

                mockMvc.perform(patch("/users/" + givenExistedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"updatedName\",\"password\":\"updatedPassword\"}"))
                        .andDo(print())
                        .andExpect(jsonPath("name").value(userUpdateData.getName()))
                        .andExpect(jsonPath("password").value(userUpdateData.getPassword()))
                        .andExpect(status().isOk());

                verify(userService).updateUser(eq(givenExistedId), any(UserUpdateData.class));
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 사용자의 아이디가 주어진다면")
        class Context_WithNotExistedIdAndUser {
            private final Long givenNotExistedId = NOT_EXISTED_ID;

            @Test
            @DisplayName("사용자를 찾을 수 없다는 메세지와 NOT_FOUND를 리턴한다")
            void itReturnsNotFoundMessageAndNOT_FOUNDHttpStatus() throws Exception {
                given(userService.updateUser(eq(givenNotExistedId), any(UserUpdateData.class)))
                        .willThrow(new UserNotFoundException(givenNotExistedId));

                mockMvc.perform(patch("/users/"+givenNotExistedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"createdUser\" , \"password\":\"createdPassword\"}"))
                        .andDo(print())
                        .andExpect(content().string(containsString("User not found")))
                        .andExpect(status().isNotFound());

                verify(userService).updateUser(eq(givenNotExistedId), any(UserUpdateData.class));
            }
        }

        @Nested
        @DisplayName("만약 비어있는 값이 주어진다면")
        class Context_WithEmpty {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("BAD_REQUEST를 리턴한다")
            void itReturnsBAD_REQUESTHttpStatus() throws Exception {
                mockMvc.perform(patch("/users/" + givenExistedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("만약 저장되어 있는 사용자의 아이디가 주어진다면")
        class Context_WithExistedId {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("주어진 아이디에 해당하는 사용자를 삭제하고 삭제된 사용자와 NO_CONTENT를 리턴한다")
            void itDeletesUserAndReturnsDeletedUserAndNO_CONTENTHttpStatus() throws Exception {
                given(userService.deleteUser(givenExistedId)).willReturn(mapper.map(setUpUser, UserResultData.class));

                mockMvc.perform(delete("/users/" + givenExistedId))
                        .andDo(print())
                        .andExpect(status().isNoContent());

                verify(userService).deleteUser(givenExistedId);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 사용자의 아이디가 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;

            @Test
            @DisplayName("사용자를 찾을 수 없다는 메세지와 NOT_FOUND를 리턴한다")
            void itReturnsNotFoundMessageAndNOT_FOUNDHttpStatus() throws Exception {
                given(userService.deleteUser(givenNotExistedId))
                        .willThrow(new UserNotFoundException(givenNotExistedId));

                mockMvc.perform(delete("/users/"+givenNotExistedId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string(containsString("User not found")))
                        .andExpect(status().isNotFound());

                verify(userService).deleteUser(givenNotExistedId);
            }
        }
    }
}


//package com.codesoom.assignment.controllers;
//
//        import com.codesoom.assignment.application.UserService;
//        import com.codesoom.assignment.domain.User;
//        import com.codesoom.assignment.dto.UserModificationData;
//        import com.codesoom.assignment.dto.UserRegistrationData;
//        import com.codesoom.assignment.errors.UserNotFoundException;
//        import org.junit.jupiter.api.BeforeEach;
//        import org.junit.jupiter.api.Test;
//        import org.springframework.beans.factory.annotation.Autowired;
//        import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//        import org.springframework.boot.test.mock.mockito.MockBean;
//        import org.springframework.http.MediaType;
//        import org.springframework.test.web.servlet.MockMvc;
//
//        import static org.hamcrest.Matchers.containsString;
//        import static org.mockito.ArgumentMatchers.any;
//        import static org.mockito.ArgumentMatchers.eq;
//        import static org.mockito.BDDMockito.given;
//        import static org.mockito.Mockito.verify;
//        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(UserController.class)
//class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        given(userService.registerUser(any(UserRegistrationData.class)))
//                .will(invocation -> {
//                    UserRegistrationData registrationData = invocation.getArgument(0);
//                    return User.builder()
//                            .id(13L)
//                            .email(registrationData.getEmail())
//                            .name(registrationData.getName())
//                            .build();
//                });
//
//
//        given(userService.updateUser(eq(1L), any(UserModificationData.class)))
//                .will(invocation -> {
//                    Long id = invocation.getArgument(0);
//                    UserModificationData modificationData =
//                            invocation.getArgument(1);
//                    return User.builder()
//                            .id(id)
//                            .email("tester@example.com")
//                            .name(modificationData.getName())
//                            .build();
//                });
//
//        given(userService.updateUser(eq(100L), any(UserModificationData.class)))
//                .willThrow(new UserNotFoundException(100L));
//
//        given(userService.deleteUser(100L))
//                .willThrow(new UserNotFoundException(100L));
//    }
//
//    @Test
//    void registerUserWithValidAttributes() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"tester@example.com\"," +
//                                "\"name\":\"Tester\",\"password\":\"test\"}")
//        )
//                .andExpect(status().isCreated())
//                .andExpect(content().string(
//                        containsString("\"id\":13")
//                ))
//                .andExpect(content().string(
//                        containsString("\"email\":\"tester@example.com\"")
//                ))
//                .andExpect(content().string(
//                        containsString("\"name\":\"Tester\"")
//                ));
//
//        verify(userService).registerUser(any(UserRegistrationData.class));
//    }
//
//    @Test
//    void registerUserWithInvalidAttributes() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{}")
//        )
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void updateUserWithValidAttributes() throws Exception {
//        mockMvc.perform(
//                patch("/users/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"TEST\",\"password\":\"test\"}")
//        )
//                .andExpect(status().isOk())
//                .andExpect(content().string(
//                        containsString("\"id\":1")
//                ))
//                .andExpect(content().string(
//                        containsString("\"name\":\"TEST\"")
//                ));
//
//        verify(userService).updateUser(eq(1L), any(UserModificationData.class));
//    }
//
//    @Test
//    void updateUserWithInvalidAttributes() throws Exception {
//        mockMvc.perform(
//                patch("/users/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"\",\"password\":\"\"}")
//        )
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void updateUserWithNotExsitedId() throws Exception {
//        mockMvc.perform(
//                patch("/users/100")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"TEST\",\"password\":\"TEST\"}")
//        )
//                .andExpect(status().isNotFound());
//
//        verify(userService)
//                .updateUser(eq(100L), any(UserModificationData.class));
//    }
//
//    @Test
//    void destroyWithExistedId() throws Exception {
//        mockMvc.perform(delete("/users/1"))
//                .andExpect(status().isNoContent());
//
//        verify(userService).deleteUser(1L);
//    }
//
//    @Test
//    void destroyWithNotExistedId() throws Exception {
//        mockMvc.perform(delete("/users/100"))
//                .andExpect(status().isNotFound());
//
//        verify(userService).deleteUser(100L);
//    }
//}
