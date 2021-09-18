package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.AccessToken;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class JwtEncoderTest {

    private static final String JWT_REGEX = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]*$";

    private JwtEncoder jwtEncoder;

    @BeforeEach
    void setUp() {
        String secret = "12345678901234567890123456789012";
        jwtEncoder = new JwtEncoder(secret);
    }

    @Nested
    @DisplayName("encode 메서드는")
    class Describe_encode {

        private User user;

        @Nested
        @DisplayName("id가 존재하는 회원이 주어지면")
        class Context_idExistUser {

            @BeforeEach
            void setUp() {
                user = User.builder()
                    .id(1L)
                    .build();
            }

            @Test
            @DisplayName("유효한 액세스 토큰을 리턴한다")
            void it_returns_validAccessToken() {
                AccessToken accessToken = jwtEncoder.encode(user);

                assertThat(Pattern.matches(JWT_REGEX, accessToken.getAccessToken()))
                    .isTrue();
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않는 회원이 주어지면")
        class Context_idNotExistUser {

            @BeforeEach
            void setUp() {
                user = User.builder()
                    .build();
            }

            @Test
            @DisplayName("유효하지 않은 액세스 토큰을 리턴한다")
            void it_returns_notValidAccessToken() {
                AccessToken accessToken = jwtEncoder.encode(user);

                assertThat(Pattern.matches(JWT_REGEX, accessToken.getAccessToken()))
                    .isFalse();
            }
        }
    }
}
