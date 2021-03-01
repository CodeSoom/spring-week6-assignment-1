package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.FakeUserRepository;
import com.codesoom.assignment.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AuthService 클래스")
class AuthServiceTest {
    private AuthService authService;
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = new FakeUserRepository();
        authService = new AuthService(userRepository);
    }

    @Nested
    @DisplayName("signIn 메서드는")
    class Describe_signIn {
        @Nested
        @DisplayName("주어진 email 의 유저가 등록되어 있을 때")
        class Context_when_exists_given_email_user {
            @Nested
            @DisplayName("주어진 password 와 등록된 유저의 password 가 일치할 때")
            class Context_when_password_is_correct {
                @Test
                @DisplayName("email 에 해당하는 유저를 리턴한다.")
                void It_returns_given_email_user() {
                    
                }
            }

            @Nested
            @DisplayName("주어진 password 와 등록된 유저의 password 가 불일치할 때")
            class Context_when_password_is_wrong {
                @Test
                @DisplayName("패스워드가 일치하지 않는다는 예외를 던진다.")
                void It_throws_wrong_password_exception() {

                }
            }
        }

        @Nested
        @DisplayName("주어진 email 의 유저가 등록되어 있지 않을 때")
        class Context_when_not_exists_given_email_user {
            @Test
            @DisplayName("유저를 찾을 수 없다는 예외를 던진다.")
            void It_throws_user_not_found_exception() {

            }
        }

    }
}
