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
    private final String SECRET_KEY = "12345678901234567890123456789010";
    private final UserRepository userRepository = mock(UserRepository.class);
    private final AuthenticationService service =
            new JwtService(new JwtUtils(SECRET_KEY), userRepository);
    private final String validEmail = "qjawlsqjacks@naver.com";
    private final String validPassword = "1234";

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
            private final UserLoginData validLoginData = new UserLoginData(validEmail, validPassword);

            @BeforeEach
            void prepare() {
                given(userRepository.findByEmail(validEmail))
                        .willReturn(Optional.of(new User(validEmail, "박범진", validPassword)));
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
            private final UserLoginData invalidLoginData = new UserLoginData("", "");

            @BeforeEach
            void prepare() {
                given(userRepository.findByEmail(""))
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
    }
}
