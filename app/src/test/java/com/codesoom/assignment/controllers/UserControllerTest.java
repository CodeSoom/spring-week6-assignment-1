package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserData;
import com.codesoom.assignment.errors.UserEmailDuplicateException;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@DisplayName("UerController 클래스")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("생성할 사용자가 있다면")
        class Context_exist_userData {

            UserData createData;

            @BeforeEach
            void setUp() throws Exception {

                createData = UserData.builder()
                        .name("name2")
                        .email("email2")
                        .password("12345")
                        .build();

                given(userService.createUser(any(UserData.class))).willReturn(any(User.class));

            }

            @Test
            @DisplayName("사용자를 등록하고 201 상태코드를 반환한다.")
            void It_create_return_200() throws Exception {

                mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createData)))
                        .andExpect(status().isCreated());

            }

        }

        @Nested
        @DisplayName("사용자가 입력한 이메일이 중복이라면")
        class Context_duplicate_email {

            UserData userData;

            @BeforeEach
            void setUp() throws Exception {

                userData = UserData.builder()
                        .name("name1")
                        .email("email1")
                        .password("12345")
                        .build();

                given(userService.createUser(any(UserData.class))).willThrow(new UserEmailDuplicateException());

            }

            @Test
            @DisplayName("BadRequest 400 상태코드를 반환한다.")
            void It_return_UserEmailDuplicateException() throws Exception {

                mockMvc.perform(post("/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userData)))
                        .andExpect(status().isBadRequest());

            }

        }

    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("수정할 사용자 내용이 있다면")
        class Context_exist_update_userData {

            UserData updateData;
            Long VALID_ID=1L;

            @BeforeEach
            void setUp() {

                updateData = UserData.builder()
                        .name("updateName")
                        .email("updateEmail")
                        .password("12345")
                        .build();

                given(userService.updateUser(VALID_ID,updateData)).will(invocation -> {

                    Long id = invocation.getArgument(0);
                    UserData source = invocation.getArgument(1);

                    return User.builder()
                            .name(source.getName())
                            .email(source.getEmail())
                            .password(source.getPassword())
                            .build();
                });

            }

            @Test
            @DisplayName("사용자 정보를 수정하고 200 상태코드를 반환한다.")
            void It_update_user_return_200() throws Exception{

                mockMvc.perform(MockMvcRequestBuilders.patch("/user/" + VALID_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateData)))
                        .andExpect(status().isOk());

            }

        }

    }

}

