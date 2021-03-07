package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginDto;
import com.codesoom.assignment.errors.InvalidUserException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
class AuthenticationServiceTest {

    private static final String VALID_EMAIL = "rhfpdk92@naver.com";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    @Value("${jwt.secret}")
    private String secret;

    private AuthenticationService authenticationService;

    private final UserRepository userRepository = mock(UserRepository.class);

    private User validUser;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(secret);

        authenticationService = new AuthenticationService(userRepository, jwtUtil);

        validUser = User.builder()
                .id(1L)
                .email(VALID_EMAIL)
                .password("password")
                .name("양승인")
                .build();

    }

    @Nested
    @DisplayName("login() 메소드는")
    class Describe_login {

        @Nested
        @DisplayName("유효한 로그인 정보를 주면")
        class Context_with_valid_login_data {
            private UserLoginDto validUserLoginDto;

            @BeforeEach
            void setUp() {
                validUserLoginDto = UserLoginDto.builder()
                        .email(validUser.getEmail())
                        .password(validUser.getPassword())
                        .build();

                given(userRepository.findByEmail(validUserLoginDto.getEmail()))
                        .willReturn(Optional.of(validUser));
            }

            @Test
            @DisplayName("액세스 토큰을 반환한다.")
            void it_return_access_token() {
                String accessToken = authenticationService.login(validUserLoginDto);
                assertThat(accessToken).contains(".");
            }
        }

        @Nested
        @DisplayName("로그인 정보에 해당하는 회원이 없으면")
        class Context_does_not_exist_user {
            private UserLoginDto invalidUserLoginDto;

            @BeforeEach
            void setUp() {
                invalidUserLoginDto = UserLoginDto.builder()
                        .email("doesNotExist@naver.com")
                        .password("password")
                        .build();

                given(userRepository.findByEmail(invalidUserLoginDto.getEmail()))
                        .willReturn(Optional.empty());
            }

            @Test
            @DisplayName("InvalidUserException을 던진다.")
            void it_return_invalid_user_exception() {
                assertThrows(InvalidUserException.class, () -> authenticationService.login(invalidUserLoginDto));
            }
        }

        @Nested
        @DisplayName("주어진 패스워드와 회원의 패스워드가 일치하지 않으면")
        class Context_do_not_match_password {
            private UserLoginDto userLoginDtoWithWrongPassword;

            @BeforeEach
            void setUp() {
                userLoginDtoWithWrongPassword = UserLoginDto.builder()
                        .email(validUser.getEmail())
                        .password("wrong password")
                        .build();

                given(userRepository.findByEmail(userLoginDtoWithWrongPassword.getEmail()))
                        .willReturn(Optional.of(validUser));
            }

            @Test
            @DisplayName("InvalidUserException을 던진다.")
            void it_return_invalid_user_exception() {
                assertThrows(InvalidUserException.class, () -> authenticationService.login(userLoginDtoWithWrongPassword));
                verify(userRepository).findByEmail(userLoginDtoWithWrongPassword.getEmail());
            }
        }

        @Nested
        @DisplayName("삭제된 회원이면 ")
        class Context_deleted_user {
            private User deletedUser;
            private UserLoginDto deletedUserLoginDto;

            @BeforeEach
            void setUp() {
                deletedUser = User.builder()
                        .id(2L)
                        .email("delete@naver.com")
                        .password("password")
                        .name("삭제된유저")
                        .deleted(true)
                        .build();
                deletedUserLoginDto = UserLoginDto.builder()
                        .email(deletedUser.getEmail())
                        .password(deletedUser.getPassword())
                        .build();

                given(userRepository.findByEmail(deletedUserLoginDto.getEmail()))
                        .willReturn(Optional.of(deletedUser));
            }

            @Test
            @DisplayName("InvalidUserException을 던진다.")
            void it_return_invalid_user_exception() {
                assertThrows(InvalidUserException.class, () -> authenticationService.login(deletedUserLoginDto));

                verify(userRepository).findByEmail(deletedUserLoginDto.getEmail());
            }
        }
    }

    @Test
    void parseToken(){
        Long userId = authenticationService.parseToken(VALID_TOKEN);
        assertThat(userId).isEqualTo(validUser.getId());
    }
}
