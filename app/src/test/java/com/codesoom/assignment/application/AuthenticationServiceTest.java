package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginDto;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.exception.DecodingInValidTokenException;
import com.codesoom.assignment.exception.NoRightPasswordException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class AuthenticationServiceTest {
    private JwtUtil jwtUtil = new JwtUtil(SECRETE);
    private final UserRepository userRepository =
            mock(UserRepository.class);
    private AuthenticationService authenticationService = new AuthenticationService(jwtUtil, userRepository);

    private static String SECRETE = "12345678901234567890123456789010";
    private static String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInJvbGUiOiJNRU1CRVIifQ.X99CPxQFtgjARrg9bqR6bQkp6JFMN9a-XUo9GAdO4so";
    private static String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInJvbGUiOiJNRU1CRVIifQ.X99CPxQFtgjARrg9bqR6bQkp6JFMN9a-XUo9GAdO4so111";

    @Nested
    @DisplayName("login 메소드는")
    class login {
        @Nested
        @DisplayName("유효한 email 과 password 가 주어지면")
        class WithValidEmailAndPassword {
            @Test
            @DisplayName("jwt token 을 응답한다.")
            void loginWithValidEmailAndPassword() {
                LoginDto loginDto = LoginDto.builder().email("test@test.com").password("test").build();

                given(userRepository.findByEmail("test@test.com")).willReturn(
                        Optional.of(User.builder().email("test@test.com").role(Role.MEMBER).id(1L).password("test")
                                .build()));
                SessionResponseData sessionResponseData = authenticationService.login(loginDto);
                assertThat(sessionResponseData.getAccessToken()).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 password 가 주어지면")
        class WithInValidEmailAndPassword {
            @Test
            @DisplayName("NoRightPasswordException 을 응답한다.")
            void loginWithValidEmailAndPassword() {
                LoginDto loginDto = LoginDto.builder().email("test@test.com").password("test1").build();

                given(userRepository.findByEmail("test@test.com")).willReturn(
                        Optional.of(User.builder().email("test@test.com").role(Role.MEMBER).id(1L).password("test")
                                .build()));
                assertThatThrownBy(() -> authenticationService.login(loginDto))
                        .isInstanceOf(NoRightPasswordException.class);
            }
        }
    }


    @Nested
    @DisplayName("decode 메소드는")
    class decode {
        @Nested
        @DisplayName("유저 아이디와 키로 인코딩한 유효한 토큰이 주어졌을 때")
        class withValidToken {
            @Test
            @DisplayName("유저 아이디를 갖고 있는 클레임을 응답한다.")
            void decodeWithValidToken() {
                Claims claims = authenticationService.decode(VALID_TOKEN);
                assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어졌을 때")
        class withInValidToken {
            @Test
            @DisplayName("DecodingInValidTokenException 을 응답한다.")
            void decodeWithValidToken() {
                assertThatThrownBy(() -> authenticationService.decode(INVALID_TOKEN))
                        .isInstanceOf(DecodingInValidTokenException.class);
            }
        }
    }

    @Nested
    @DisplayName("decode 메소드는")
    class parseRole {
        @Nested
        @DisplayName("유저 아이디와 키로 인코딩한 유효한 토큰이 주어졌을 때")
        class withValidToken {
            @Test
            @DisplayName("유저 아이디를 갖고 있는 클레임을 응답한다.")
            void parseRoleWithValidToken() {
                Role role = authenticationService.parseUserRole(VALID_TOKEN);
                assertThat(role).isEqualTo(Role.MEMBER);
            }
        }
    }
}
