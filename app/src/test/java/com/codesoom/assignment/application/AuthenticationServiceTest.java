package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * AuthenticationService에 대한 테스트 클래스
 */
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";
    private static final Long EXISTING_ID = 1L;
    private static final Long NON_EXISTING_ID = 100L;

    private AuthenticationService authenticationService;
    private UserService userService;

    private JwtUtil jwtUtil;
    private String token;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);

        authenticationService = new AuthenticationService(jwtUtil, userService);
    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login_method {
        @Nested
        @DisplayName("존재하는 id가 주어졌을 경우")
        class Context_if_existing_id_given {
            @Nested
            @DisplayName("유효한 토큰을 반환한다")
            class It_returns_valid_tokeng {
                String token() {
                    return authenticationService.login(EXISTING_ID);
                }

                @Test
                void test() {
                    assertThat(token()).isEqualTo(VALID_TOKEN);
                }
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어졌을 경우")
        class Context_if_non_existing_id_given {
            @Nested
            @DisplayName("UserNotFoundException 예외를 던진다")
            class It_throws_userNotFoundException {
                String token() {
                    return authenticationService.login(NON_EXISTING_ID);
                }

                @Test
                void test() {
                    assertThatThrownBy(() -> token())
                            .isInstanceOf(UserNotFoundException.class);
                }
            }
        }
    }
}
