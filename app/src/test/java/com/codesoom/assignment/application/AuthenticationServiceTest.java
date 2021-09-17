package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.errors.EmailNotFoundException;
import com.codesoom.assignment.errors.WrongPasswordException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";
    public static final String INVALID_EMAIL = "invalidEmail";

    private AuthenticationService authenticationService;

    private final UserRepository userRepository = mock(UserRepository.class);


    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil, userRepository);
    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login{

        @Nested
        @DisplayName("이메일이 존재하지 않을 경우")
        class Context_with_not_exist_email{

            SessionRequestData reqData = SessionRequestData.builder()
                    .email("email")
                    .password("password")
                    .build();

            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(reqData.getEmail()))
                        .willReturn(Optional.empty());
            }

            @Test
            @DisplayName("WrongEmailOrPasswordException을 던진다.")
            void it_throws_WrongEmailOrPasswordException() {
                assertThatThrownBy(()->authenticationService.login(reqData))
                        .isInstanceOf(EmailNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("이메일이 존재할 경우")
        class Context_with_exist_email{

            @BeforeEach
            void setUp() {
                given(userRepository.existsByEmail(reqData.getEmail())).willReturn(false);
            }

            String email = "email";
            String validPassword = "validPassword";
            String invalidPassword = "invalidPassword";

            SessionRequestData reqData = SessionRequestData.builder()
                    .email(email)
                    .password(validPassword)
                    .build();

            @Nested
            @DisplayName("패스워드가 옳바르지 않은 경우")
            class Context_with_wrong_password{

                @BeforeEach
                void setUp() {
                    given(userRepository.findByEmail(reqData.getEmail()))
                            .willReturn(Optional.of(
                                    User.builder()
                                            .email(email)
                                            .password(invalidPassword)
                                            .build()));
                }

                @Test
                @DisplayName("WrongEmailOrPasswordException을 던진다.")
                void it_throws_WrongEmailOrPasswordException() {
                    assertThatThrownBy(() -> authenticationService.login(reqData))
                            .isInstanceOf(WrongPasswordException.class);
                }
            }

            @Nested
            @DisplayName("패스워드가 옳바른 경우")
            class Context_with_valid_password{

                @BeforeEach
                void setUp() {
                    given(userRepository.findByEmail(reqData.getEmail()))
                            .willReturn(Optional.of(
                                    User.builder()
                                            .email(email)
                                            .password(validPassword)
                                            .build()));
                }

                @Test
                @DisplayName("jwt 토큰을 반환할 것이다.")
                void it_returns_jwt() {
                    String result = authenticationService.login(reqData);

                    String[] results = result.split("\\.");

                    assertThat(results[0]).isBase64();
                    assertThat(results[1]).isBase64();
                    assertThat(results[2]).isBase64();

                    //todo 테스트 성공필요..
                    //assertThatThrownBy(() -> results[3]).isInstanceOf(IndexOutOfBoundsException.class);
                }
            }
        }
    }



    @Test
    void parseTokenWithValidToken() {
        Long userId = authenticationService.parseToken(VALID_TOKEN);
        assertThat(userId).isEqualTo(1L);
    }
}
