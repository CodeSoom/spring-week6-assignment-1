package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SessionController 테스트")
class SessionControllerTest {
    private static final String SECRET = "12345678901234567890123456789010";

    private final static String USER_NAME = "곽형조";
    private final static String USER_EMAIL = "rhkrgudwh@test.com";
    private final static String USER_PASSWORD = "asdqwe1234";

    private JwtUtil jwtUtil = new JwtUtil(SECRET);

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private Mapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User existedUser;
    private String existedUserToken;

    @BeforeEach
    void setUpMockMvc() {
        this.mapper = DozerBeanMapperBuilder.buildDefault();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    void prepareUser() {
        UserRegistrationData registrationData = UserRegistrationData.builder()
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        existedUser = mapper.map(registrationData, User.class);

        userRepository.save(existedUser);

        existedUserToken = jwtUtil.encode(
                existedUser.getId(),
                existedUser.getName(),
                existedUser.getEmail()
        );
    }

    @Nested
    @DisplayName("POST /session 요청은")
    class Describe_post_session {
        private String requestContent;

        @Nested
        @DisplayName("존재하는 user 정보가 주어지면")
        class Context_with_user_data {

            @BeforeEach
            void prepare() throws JsonProcessingException {
                prepareUser();

                LoginRequestData loginRequestData = LoginRequestData.builder()
                        .email(existedUser.getEmail())
                        .password(existedUser.getPassword())
                        .build();
                requestContent = objectMapper.writeValueAsString(loginRequestData);
            }

            @Test
            @DisplayName("accessToken 을 응답한다")
            void login() throws Exception {
                mockMvc.perform(
                                post("/session")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestContent)
                        )
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("accessToken").value(existedUserToken));
            }
        }
    }
}