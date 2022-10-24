package com.codesoom.assignment.controllers;

import com.codesoom.assignment.common.utils.JwtUtil;
import com.codesoom.assignment.domain.user.User;
import com.codesoom.assignment.domain.user.UserRepository;
import com.codesoom.assignment.dto.AuthDto;
import com.codesoom.assignment.utils.LoginSampleFactory;
import com.codesoom.assignment.utils.UserSampleFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static com.codesoom.assignment.utils.LoginSampleFactory.FieldName.EMAIL;
import static com.codesoom.assignment.utils.LoginSampleFactory.FieldName.PASSWORD;
import static com.codesoom.assignment.utils.LoginSampleFactory.ValueType.BLANK;
import static com.codesoom.assignment.utils.LoginSampleFactory.ValueType.EMPTY;
import static com.codesoom.assignment.utils.LoginSampleFactory.ValueType.NULL;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AuthenticationController 클래스")
class AuthenticationControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        // ResponseBody JSON에 한글이 깨지는 문제로 추가
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Nested
    @DisplayName("login[/session::POST] 메소드는")
    class Describe_login {
        private final User existUser = userRepository.save(UserSampleFactory.createUser());
        private final Long EXIST_USER_ID = existUser.getId();
        private final String VALID_TOKEN = jwtUtil.encode(EXIST_USER_ID);

        ResultActions subject(AuthDto.LoginParam request) throws Exception {
            return mockMvc.perform(post("/session")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        }

        @Nested
        @DisplayName("유효한 이메일과 비밀번호가 주어지면")
        class Context_with_valid_email_and_password {
            private final AuthDto.LoginParam givenLogin = new AuthDto.LoginParam();

            @BeforeEach
            void prepare() {
                System.out.println("existUser = " + existUser);
                givenLogin.setEmail(existUser.getEmail());
                givenLogin.setPassword(existUser.getPassword());
            }

            @Test
            @DisplayName("CREATED(201)와 토큰을 리턴한다")
            void it_returns_201_and_token() throws Exception {
                final ResultActions resultActions = subject(givenLogin);

                resultActions.andExpect(status().isCreated())
                        .andExpect(jsonPath("accessToken").value(equalTo(VALID_TOKEN)))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("필수항목이 Null이면")
        class Context_with_null {
            private final List<AuthDto.LoginParam> loginParamList = new ArrayList<>();

            @BeforeEach
            void prepare() {
                loginParamList.add(LoginSampleFactory.createRequestParamWith(EMAIL, NULL));
                loginParamList.add(LoginSampleFactory.createRequestParamWith(EMAIL, EMPTY));
                loginParamList.add(LoginSampleFactory.createRequestParamWith(EMAIL, BLANK));
                loginParamList.add(LoginSampleFactory.createRequestParamWith(PASSWORD, NULL));
                loginParamList.add(LoginSampleFactory.createRequestParamWith(PASSWORD, EMPTY));
                loginParamList.add(LoginSampleFactory.createRequestParamWith(PASSWORD, BLANK));
                loginParamList.add(null);
            }

            @Test
            @DisplayName("BAD_REQUEST(400)와 에러메시지를 리턴한다")
            void it_returns_400_and_error_message() {
                loginParamList.forEach(this::test);
            }

            private void test(AuthDto.LoginParam request) {
                try {
                    ResultActions resultActions = subject(request);

                    resultActions.andExpect(status().isBadRequest())
                            .andDo(print());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Nested
        @DisplayName("존재하지않는 이메일이 주어지면")
        class Context_with_invalid_email {
            private final AuthDto.LoginParam notExistUser = new AuthDto.LoginParam();

            @BeforeEach
            void prepare() {
                notExistUser.setEmail("notexist@test.com");
                notExistUser.setPassword("test");
            }

            @Test
            @DisplayName("NOT_FOUND(404)를 리턴한다")
            void it_returns_404() throws Exception {
                ResultActions resultActions = subject(notExistUser);

                resultActions.andExpect(status().isNotFound())
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("잘못된 비밀번호가 주어지면")
        class Context_with_wrong_password {
            private final AuthDto.LoginParam wrongPasswordUser = new AuthDto.LoginParam();

            @BeforeEach
            void prepare() {
                wrongPasswordUser.setEmail(existUser.getEmail());
                wrongPasswordUser.setPassword(existUser.getPassword() + "wrong");
            }

            @Test
            @DisplayName("BAD_REQUEST(400)를 리턴한다")
            void it_returns_400() throws Exception {
                ResultActions resultActions = subject(wrongPasswordUser);

                resultActions.andExpect(status().isBadRequest())
                        .andDo(print());
            }
        }
    }
}