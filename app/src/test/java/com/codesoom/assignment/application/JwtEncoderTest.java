package com.codesoom.assignment.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.AccessToken;
import java.util.ArrayList;
import java.util.List;
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

        private List<User> users = new ArrayList<>();

        @Nested
        @DisplayName("id가 존재하는 회원이 주어지면")
        class Context_idExistUser {

            @BeforeEach
            void setUp() {
                for (long i = 1; i <= 10; i++) {
                    users.add(User.builder()
                        .id(i)
                        .build());
                }
            }

            @Test
            @DisplayName("유효한 액세스 토큰을 리턴한다")
            void it_returns_validAccessToken() {
                for (User validUser : users) {
                    assertThat(checkUserIsValid(validUser)).isTrue();
                }
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않는 회원이 주어지면")
        class Context_idNotExistUser {

            @BeforeEach
            void setUp() {
                for (int i = 1; i <= 10; i++) {
                    users.add(User.builder()
                        .build());
                }
            }

            @Test
            @DisplayName("유효하지 않은 액세스 토큰을 리턴한다")
            void it_returns_notValidAccessToken() {
                for (User invalidUser : users) {
                    assertThat(checkUserIsValid(invalidUser)).isFalse();
                }
            }
        }

        private boolean checkUserIsValid(User validUser) {
            AccessToken accessToken = jwtEncoder.encode(validUser);

            return Pattern.matches(JWT_REGEX, accessToken.getAccessToken());
        }
    }
}
