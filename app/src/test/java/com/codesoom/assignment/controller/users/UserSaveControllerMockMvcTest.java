package com.codesoom.assignment.controller.users;

import com.codesoom.assignment.controller.ControllerTest;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.domain.users.UserResponseDto;
import com.codesoom.assignment.domain.users.UserSaveDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("UserSaveController 클래스")
public class UserSaveControllerMockMvcTest extends ControllerTest {

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

    @DisplayName("[POST] /users")
    @Nested
    class Describe_save_user {

        @DisplayName("필수 입력 값을 모두 입력하면")
        @Nested
        class Context_with_valid_data {

            private final UserSaveDto VALID_USER_SAVE_DTO
                    = new UserSaveDto("홍길동", "hkd@codesoom.com", "password");

            @DisplayName("회원 정보를 성공적으로 저장한다.")
            @Test
            void it_save_user() throws Exception {

                final MvcResult result = mockMvc.perform(post("/users")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(VALID_USER_SAVE_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();

                final UserResponseDto userResponseDto
                        = objectMapper.readValue(result.getResponse().getContentAsByteArray(), UserResponseDto.class);
                assertThat(repository.findById(userResponseDto.getId())).isNotEmpty();
            }
        }

        @DisplayName("이름을 입력하지 않으면")
        @Nested
        class Context_with_empty_name {

            private final UserSaveDto EMPTY_NAME
                    = new UserSaveDto("", "hkd@codesoom.com", "password");
            private final UserSaveDto BLANK_NAME
                    = new UserSaveDto(" ", "hkd@codesoom.com", "password");
            private final UserSaveDto NULL_NAME
                    = new UserSaveDto(null, "hkd@codesoom.com", "password");
            private final List<UserSaveDto> INVALID_NAMES = List.of(EMPTY_NAME, BLANK_NAME, NULL_NAME);

            @DisplayName("400 bad request를 응답한다.")
            @Test
            void it_response_bad_request() throws Exception {
                for (UserSaveDto invalidNameUserDto : INVALID_NAMES) {
                    mockMvc.perform(post("/users").accept(MediaType.APPLICATION_JSON_UTF8)
                            .content(objectMapper.writeValueAsString(invalidNameUserDto))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isBadRequest());
                }
            }
        }

        @DisplayName("이메일을 입력하지 않거나, 이메일 형식을 지키지 않으면")
        @Nested
        class Context_with_empty_email {

            private final UserSaveDto EMPTY_EMAIL
                    = new UserSaveDto("홍길동", "", "password");
            private final UserSaveDto BLANK_EMAIL
                    = new UserSaveDto("Kyrie Irving",  " ", "earthisflat");
            private final UserSaveDto NULL_EMAIL
                    = new UserSaveDto("제갈길동", null, "jaegal0909!");
            private final UserSaveDto INVALID_EMAIL
                    = new UserSaveDto("홍 길동", "itsNotEmailFormat@", "password");
            private final List<UserSaveDto> INVALID_EMAILS
                    = List.of(EMPTY_EMAIL, BLANK_EMAIL, NULL_EMAIL, INVALID_EMAIL);

            @DisplayName("400 bad request를 응답한다.")
            @Test
            void it_response_bad_request() throws Exception {
                for (UserSaveDto invalidEmailUserDto : INVALID_EMAILS) {
                    mockMvc.perform(post("/users").accept(MediaType.APPLICATION_JSON_UTF8)
                            .content(objectMapper.writeValueAsString(invalidEmailUserDto))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isBadRequest());
                }
            }
        }

        @DisplayName("비밀번호를 입력하지 않거나, 비밀번호 형식을 지키지 않으면")
        @Nested
        class Context_with_empty_password {

            private final UserSaveDto EMPTY_PASSWORD
                    = new UserSaveDto("러셀 웨스트브룩", "why@not.zero", "");
            private final UserSaveDto BLANK_PASSWORD
                    = new UserSaveDto("김철수", "hkd@gmail.com", " ");
            private final UserSaveDto NULL_PASSWORD
                    = new UserSaveDto("안영희", "hkd_lil@naver.com", null);
            private final UserSaveDto TOO_SHORT_PASSWORD
                    = new UserSaveDto("엉클 밥", "hkd423@yahoo.com", "1234");
            private final UserSaveDto TOO_LONG_PASSWORD
                    = new UserSaveDto("강백호", "hkd@codesoom.com"
                    , "qwertyuiopasdfghjklzxcvbnm123456789");
            private final List<UserSaveDto> INVALID_PASSWORDS
                    = List.of(EMPTY_PASSWORD, BLANK_PASSWORD, NULL_PASSWORD
                    ,TOO_SHORT_PASSWORD, TOO_LONG_PASSWORD);

            @DisplayName("400 bad request를 응답한다.")
            @Test
            void it_response_bad_request() throws Exception {
                for (UserSaveDto invalidPasswordUserDto : INVALID_PASSWORDS) {
                    mockMvc.perform(post("/users").accept(MediaType.APPLICATION_JSON_UTF8)
                            .content(objectMapper.writeValueAsString(invalidPasswordUserDto))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isBadRequest());
                }
            }
        }
    }

}
