package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.UnauthorizedException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

class AuthenticationServiceTest {

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = VALID_TOKEN + "invalid";

    @Value("${jwt.secret}")
    private static String SECRETE_KEY;
    private User user;
    private UserLoginData loginData;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRETE_KEY);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        authenticationService = new AuthenticationService(jwtUtil, userRepository);

        loginData = UserLoginData.builder()
                .email("test@test.codesoom")
                .password("password")
                .build();

        user = User.builder()
                .email("test@test.codesoom")
                .password("password")
                .build();

    }


    @Nested
    @DisplayName("parseToken메소드는")
    class Describe_parseToken {
        @Nested
        @DisplayName("유효한 토큰이 주어질 때")
        class Context_WithValidToken {

            @DisplayName("userId를 반환한다.")
            @Test
            void it_ReturnsUserId() {
                Long userId = authenticationService.parsetoken(VALID_TOKEN);
                System.out.println("userId = " + userId);
                assertThat(userId).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("유효하지않은 토큰이 주어질 때")
        class Context_WithInValidToken {

            @DisplayName("UnauthorizedException을 던진다.")
            @Test
            void it_ReturnsUserId() {
                assertThatThrownBy(
                        () -> authenticationService.parsetoken(INVALID_TOKEN)
                ).isInstanceOf(UnauthorizedException.class);
            }
        }

        @Nested
        @DisplayName("토큰이 비어있을 때")
        class Context_WithBlank {

            @DisplayName("UnauthorizedException을 던진다.")
            @Test
            void parseTokenWithBlank() {
                assertThatThrownBy(
                        () -> authenticationService.parsetoken(null)
                ).isInstanceOf(UnauthorizedException.class);


                assertThatThrownBy(
                        () -> authenticationService.parsetoken("")
                ).isInstanceOf(UnauthorizedException.class);
            }
        }
    }



    @Nested
    @DisplayName("createToken 메소드는 ")
    class Describe_createToken {

        @Nested
        @DisplayName("유효한 로그인 데이터가 주어졌을 때")
        class Context_WithValidUserLoginData {
            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(loginData.getEmail()))
                        .willReturn(Optional.of(user));
            }


            @DisplayName("jwt토큰을 반환한다.")
            @Test
            void createToken() {
                String accessToken = authenticationService.createToken(loginData);
                assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }
        }
    }
}
