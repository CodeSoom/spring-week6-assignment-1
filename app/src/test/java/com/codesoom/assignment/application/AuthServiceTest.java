package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.FakeUserRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.errors.WrongUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AuthService 클래스")
class AuthServiceTest {
    private final Long givenID = 1L;
    private final String givenEmail = "juuni.ni.i@gmail.com";
    private final String givenName = "juunini";
    private final String givenPassword = "secret";
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
            private final User givenUser = new User(
                    1L,
                    givenEmail,
                    "juunini",
                    givenPassword,
                    false
            );

            @BeforeEach
            void setup() {
                userRepository.save(givenUser);
            }

            @Nested
            @DisplayName("주어진 password 와 등록된 유저의 password 가 일치할 때")
            class Context_when_password_is_correct {

                @Test
                @DisplayName("email 에 해당하는 유저를 리턴한다.")
                void It_returns_given_email_user() {
                    User user = authService.signIn(givenEmail, givenPassword);

                    assertThat(user.getEmail()).isEqualTo(givenEmail);
                    assertThat(user.getPassword()).isEqualTo(givenPassword);
                }
            }

            @Nested
            @DisplayName("주어진 password 와 등록된 유저의 password 가 불일치할 때")
            class Context_when_password_is_wrong {
                @Test
                @DisplayName("패스워드가 일치하지 않는다는 예외를 던진다.")
                void It_throws_wrong_password_exception() {
                    assertThatThrownBy(() -> authService.signIn(givenEmail, "wrong password"))
                            .isInstanceOf(WrongUserException.class);
                }
            }
        }

        @Nested
        @DisplayName("주어진 email 의 유저가 등록되어 있지 않을 때")
        class Context_when_not_exists_given_email_user {
            @Test
            @DisplayName("유저를 찾을 수 없다는 예외를 던진다.")
            void It_throws_user_not_found_exception() {
                assertThatThrownBy(() -> authService.signIn(givenEmail, "wrong password"))
                        .isInstanceOf(WrongUserException.class);
            }
        }
    }

    @Nested
    @DisplayName("valid 메서드는")
    class Describe_valid {
        @Nested
        @DisplayName("주어진 유저의 정보가 올바를 때")
        class Context_with_valid_given_user_information {
            private final User givenUser = new User(givenID, givenEmail, givenName, givenPassword, false);

            @BeforeEach
            void setup() {
                userRepository.save(givenUser);
            }

            @Test
            @DisplayName("true 를 리턴한다.")
            void It_returns_true() {
                final Boolean valid = authService.valid(givenID, givenEmail, givenName);

                assertThat(valid).isTrue();
            }
        }

        @Nested
        @DisplayName("주어진 유저의 정보가 올바르지 않을 때")
        class Context_without_valid_given_user_information {
            private final User givenUser1 = new User(
                    1L,
                    "invalid@mail.com",
                    givenName,
                    givenPassword,
                    false
            );

            private final User givenUser2 = new User(
                    2L,
                    givenEmail,
                    "unknown",
                    givenPassword,
                    false
            );

            @BeforeEach
            void setup() {
                userRepository.save(givenUser1);
                userRepository.save(givenUser2);
            }

            @Test
            @DisplayName("false 를 리턴한다.")
            void It_returns_false() {
                final Boolean invalidEmail = authService.valid(givenUser1.getId(), givenEmail, givenName);
                final Boolean invalidName = authService.valid(givenUser2.getId(), givenEmail, givenName);

                assertThat(invalidEmail).isFalse();
                assertThat(invalidName).isFalse();
            }
        }

        @Nested
        @DisplayName("등록되지 않은 유저일 때")
        class Context_without_registered_user {
            @Test
            @DisplayName("유저를 찾을 수 없다는 예외를 던진다.")
            void It_returns_false() {
                assertThatThrownBy(() -> authService.valid(givenID, givenEmail, givenName))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }
}
