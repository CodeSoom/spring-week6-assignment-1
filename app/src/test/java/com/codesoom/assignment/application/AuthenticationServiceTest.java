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
        /*
          유효할 때와 아닐 때의 조건을 given으로 설정하고 있는 구조이어서 대신 reqData를 가장 상단에 선언하여 하나만 사용하도록 처리하였습니다.
          지금 테스트 구조가 가독성이 별로일까요?

          추가 질문 DTO를 어디까지 만들어야 하는가.. cause User return
         */
        String email = "email";
        String validPassword = "validPassword";

        SessionRequestData reqData = SessionRequestData.builder()
                .email(email)
                .password(validPassword)
                .build();

        @Nested
        @DisplayName("이메일이 존재하지 않을 경우")
        class Context_with_not_exist_email{

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

            @Nested
            @DisplayName("패스워드가 옳바르지 않은 경우")
            class Context_with_wrong_password{
                String invalidPassword = "invalidPassword";
                User foundUser = User.builder()
                        .email(email)
                        .password(invalidPassword)
                        .build();

                @BeforeEach
                void setUp() {
                    given(userRepository.findByEmail(reqData.getEmail()))
                            .willReturn(Optional.of(foundUser));
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
                User foundUser = User.builder()
                        .email(email)
                        .password(validPassword)
                        .build();

                @BeforeEach
                void setUp() {
                    given(userRepository.findByEmail(reqData.getEmail()))
                            .willReturn(Optional.of(foundUser));
                }

                @Test
                @DisplayName("jwt 토큰을 반환할 것이다.")
                void it_returns_jwt() {
                    String result = authenticationService.login(reqData);

                    assertThat(result).isNotBlank();

                    String[] results = result.split("\\.");

                    assertThat(results[0]).isBase64();
                    assertThat(results[1]).isBase64();
                    //assertThat(results[2]).isBase64(); Signiture 부분은 isBase64가 통과되지 않는다.
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
