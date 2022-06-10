package com.codesoom.assignment.application;


import com.codesoom.assignment.auth.JwtAuth;
import com.codesoom.assignment.auth.JwtKey;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.infra.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("JwtAuthService")
class JwtAuthServiceTest {
    @Autowired
    private JpaUserRepository userRepository;

    private JwtAuthService service;

    private final String SECRET_STRING = "12345678901234567890123456789010";
    private final Long USER_ID = 1L;
    private final String EMAIL = "test@gmail.com";
    private final String PASSWORD = "test0012300";
    private final JwtKey key = new JwtKey(SECRET_STRING);
    private final JwtAuth auth = new JwtAuth(key);

    @BeforeEach
    void setUp() {
        service = new JwtAuthService(auth, userRepository);
    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {

        @DataJpaTest
        @Nested
        @DisplayName("유효한 User Entity를 전달 받으면")
        class Context_with_valid_user_entity {
            private User user;

            @BeforeEach
            void setUp() {
                user = User.builder()
                        .id(USER_ID)
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build();
                userRepository.save(user);
            }

            @Test
            @DisplayName("문자열 타입의 토큰을 반환한다.")
            void it_returns_string_token() {
                String token = service.login(user);

                assertThat(token).isInstanceOf(String.class);
            }
        }
    }
}
