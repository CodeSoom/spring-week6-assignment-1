package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidInformationException;
import com.codesoom.assignment.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("AuthenticationService 인터페이스의")
public class AuthenticationServiceTest {
    public static final String VALID_NAME = "박범진";
    private static final String VALID_EMAIL = "qjawlsqjacks@naver.com";
    private static final String VALID_PW = "1234";
    public static final String INVALID_EMAIL = "";
    public static final String INVALID_PW = " ";
    private static final UserLoginData validLoginData = new UserLoginData(VALID_EMAIL, VALID_PW);
    private static final UserLoginData invalidLoginData = new UserLoginData(INVALID_EMAIL, INVALID_PW);

    private final String SECRET_KEY = "12345678901234567890123456789010";
    private final UserRepository userRepository = mock(UserRepository.class);
    private final AuthenticationService service =
            new JwtService(new JwtUtils(SECRET_KEY), userRepository);

    private Claims decodingResult(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {
        @Nested
        @DisplayName("유효한 유저 정보가 주어지면")
        class Context_with_validUserData {
            private final UserLoginData validLoginData = new UserLoginData(VALID_EMAIL, VALID_PW);

            @BeforeEach
            void prepare() {
                given(userRepository.findByEmail(VALID_EMAIL))
                        .willReturn(Optional.of(new User(VALID_EMAIL, VALID_NAME, VALID_PW)));
            }

            @Test
            @DisplayName("토큰을 리턴한다")
            void It_returns_token() {
                String expectToken = service.login(validLoginData);

                assertThat(validLoginData.getEmail())
                        .isEqualTo(decodingResult(expectToken).get("email"));
            }
        }

        @Nested
        @DisplayName("유효하지 않은 유저 정보가 주어지면")
        class Context_with_invalidUserData {
            @BeforeEach
            void prepare() {
                given(userRepository.findByEmail(INVALID_EMAIL))
                        .willReturn(Optional.empty());
            }

            @Test
            @DisplayName("예외를 던진다")
            void It_throws_exception() {
                assertThatThrownBy(() -> service.login(invalidLoginData))
                        .isInstanceOf(RuntimeException.class)
                        .isExactlyInstanceOf(InvalidInformationException.class);
            }
        }

        @Nested
        @DisplayName("주어진 유저가 유효하지 않은 값을 가지고 있다면")
        class Context_with_userWithInvalidValue {
            @BeforeEach
            void prepare() {
                given(userRepository.findByEmail(VALID_EMAIL))
                        .willReturn(Optional.of(new User(null, null, null)));
            }

            @Test
            @DisplayName("예외를 던진다")
            void It_throws_exception() {
                assertThatThrownBy(() -> service.login(validLoginData))
                        .isInstanceOf(RuntimeException.class)
                        .isExactlyInstanceOf(InvalidInformationException.class);
            }
        }
    }
}
