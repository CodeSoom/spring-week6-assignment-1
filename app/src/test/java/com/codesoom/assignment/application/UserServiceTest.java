package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.errors.UserEmailDuplicationException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.errors.UserPasswordMismatchException;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("UserService 클래스")
class UserServiceTest {
    private static final Long USER_ID = 1L;
    private static final Long DELETED_USER_ID = 3L;
    private static final Long UNKNOWN_USER_ID = 9999L;

    private static final String EMAIL = "sample@email.com";
    private static final String EXISTED_EMAIL_ADDRESS = "existed@example.com";
    private static final String UNKNOWN_EMAIL = EMAIL + "UNKNOWN";

    private static final String PASSWORD = "sample_password";
    private static final String WRONG_PASSWORD = PASSWORD + "WRONG";

    private static final String NAME = "sample";

    private UserService userService;

    private final UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();

        userService = new UserService(mapper, userRepository);

        given(userRepository.existsByEmail(EXISTED_EMAIL_ADDRESS))
                .willReturn(true);

        given(userRepository.save(any(User.class))).will(invocation -> {
            User source = invocation.getArgument(0);
            return User.builder()
                    .id(13L)
                    .email(source.getEmail())
                    .name(source.getName())
                    .build();
        });

        given(userRepository.findByIdAndDeletedIsFalse(USER_ID))
                .willReturn(Optional.of(
                        User.builder()
                                .id(USER_ID)
                                .email(EXISTED_EMAIL_ADDRESS)
                                .name("Tester")
                                .password("test")
                                .build()));

        given(userRepository.findByIdAndDeletedIsFalse(UNKNOWN_USER_ID))
                .willReturn(Optional.empty());

        given(userRepository.findByIdAndDeletedIsFalse(DELETED_USER_ID))
                .willReturn(Optional.empty());

        given(userRepository.findUserByEmail(EMAIL))
                .willReturn(Optional.of(
                        User.builder()
                                .id(USER_ID)
                                .email(EMAIL)
                                .password(PASSWORD)
                                .name(NAME)
                                .build()));

        given(userRepository.findUserByEmail(UNKNOWN_EMAIL))
                .willReturn(Optional.empty());
    }

    @Nested
    @DisplayName("registerUser 메서드")
    class DescribeRegisterUser {
        @Nested
        @DisplayName("신규 회원인 경우")
        class ContextWithUnregisteredUser {
            @Test
            @DisplayName("신규 가입된 회원을 반환한다")
            void itReturnsNewRegistedUser() {
                UserRegistrationData registrationData = UserRegistrationData.builder()
                        .email("tester@example.com")
                        .name("Tester")
                        .password("test")
                        .build();

                User user = userService.registerUser(registrationData);

                assertThat(user.getId()).isEqualTo(13L);
                assertThat(user.getEmail()).isEqualTo("tester@example.com");
                assertThat(user.getName()).isEqualTo("Tester");

                verify(userRepository).save(any(User.class));
            }
        }

        @Nested
        @DisplayName("이미 등록된 회원인 경우")
        class ContextWithRegisteredUser {
            @Test
            @DisplayName("UserEmailDuplicationException을 반환한다")
            void registerUserWithDuplicatedEmail() {
                UserRegistrationData registrationData = UserRegistrationData.builder()
                        .email(EXISTED_EMAIL_ADDRESS)
                        .name("Tester")
                        .password("test")
                        .build();

                assertThatThrownBy(() -> userService.registerUser(registrationData))
                        .isInstanceOf(UserEmailDuplicationException.class);

                verify(userRepository).existsByEmail(EXISTED_EMAIL_ADDRESS);
            }
        }
    }

    @Nested
    @DisplayName("updateUser 메서드")
    class DescribeUpdateUser {
        @Nested
        @DisplayName("존재하는 회원의 식별자인 경우")
        class ContextWithExistedUser {
            @Test
            @DisplayName("회원 정보를 갱신하고, 갱신한 회원을 반환한다")
            void itReturnsUpdatedUser() {
                UserModificationData modificationData = UserModificationData.builder()
                        .name("TEST")
                        .password("TEST")
                        .build();

                User user = userService.updateUser(1L, modificationData);

                assertThat(user.getId()).isEqualTo(1L);
                assertThat(user.getEmail()).isEqualTo(EXISTED_EMAIL_ADDRESS);
                assertThat(user.getName()).isEqualTo("TEST");

                verify(userRepository).findByIdAndDeletedIsFalse(1L);
            }

            @Test
            @DisplayName("삭제 처리된 ID라면, UserNotFoundException을 반환한다")
            void updateUserWithDeletedId() {
                UserModificationData modificationData = UserModificationData.builder()
                        .name("TEST")
                        .password("TEST")
                        .build();

                assertThatThrownBy(
                        () -> userService.updateUser(DELETED_USER_ID, modificationData)
                )
                        .isInstanceOf(UserNotFoundException.class);

                verify(userRepository).findByIdAndDeletedIsFalse(DELETED_USER_ID);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 회원의 식별자인 경우")
        class ContextWithNotExistedUser {
            @Test
            @DisplayName("UserNotFoundException을 반환한다")
            void updateUserWithNotExistedId() {
                UserModificationData modificationData = UserModificationData.builder()
                        .name("TEST")
                        .password("TEST")
                        .build();

                assertThatThrownBy(
                        () -> userService.updateUser(UNKNOWN_USER_ID, modificationData)
                )
                        .isInstanceOf(UserNotFoundException.class);

                verify(userRepository).findByIdAndDeletedIsFalse(UNKNOWN_USER_ID);
            }
        }
    }

    @Nested
    @DisplayName("deleteUser 메서드")
    class DescribeDeleteUser {
        @Nested
        @DisplayName("존재하는 회원의 식별자인 경우")
        class ContextWithExistedUser {
            @Test
            @DisplayName("회원 삭제 처리한다")
            void itDeletesUser() {
                User user = userService.deleteUser(1L);

                assertThat(user.getId()).isEqualTo(1L);
                assertThat(user.isDeleted()).isTrue();

                verify(userRepository).findByIdAndDeletedIsFalse(1L);
            }

            @Test
            @DisplayName("삭제 처리된 ID라면, UserNotFoundException을 반환한다")
            void itReturnsUserNotFoundExceptionWithDeletedId() {
                assertThatThrownBy(() -> userService.deleteUser(DELETED_USER_ID))
                        .isInstanceOf(UserNotFoundException.class);

                verify(userRepository).findByIdAndDeletedIsFalse(DELETED_USER_ID);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 회원의 식별자인 경우")
        class ContextWithNotExistedUser {
            @Test
            @DisplayName("UserNotFoundException을 반환한다")
            void itReturnsUserNotFoundExceptionWithUnknownId() {
                assertThatThrownBy(() -> userService.deleteUser(UNKNOWN_USER_ID))
                        .isInstanceOf(UserNotFoundException.class);

                verify(userRepository).findByIdAndDeletedIsFalse(UNKNOWN_USER_ID);
            }
        }
    }

    @Nested
    @DisplayName("findUserByEmailPassword 메서드")
    class DescribeFindUserByEmailPassword {
        @Nested
        @DisplayName("존재하는 회원의 이메일인 경우")
        class ContextWithExistedUserEmail {
            @Test
            @DisplayName("비밀번호가 일치한다면, 해당 회원을 반환한다")
            void itReturnsUser() {
                User user = userService.findUserByEmailPassword(EMAIL, PASSWORD);

                assertThat(user.getId()).isNotNull().isInstanceOf(Long.class);
                assertThat(user.getEmail()).isEqualTo(EMAIL);
                assertThat(user.getPassword()).isEqualTo(PASSWORD);

                verify(userRepository).findUserByEmail(EMAIL);
            }

            @Test
            @DisplayName("비밀번호가 불일치한다면, UserPasswordMismatchException을 반환한다")
            void itReturnsUserPasswordMismatchException() {
                assertThatThrownBy(() -> userService.findUserByEmailPassword(EMAIL, WRONG_PASSWORD))
                        .isInstanceOf(UserPasswordMismatchException.class);

                verify(userRepository).findUserByEmail(EMAIL);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 회원의 이메일인 경우")
        class ContextWithUnknownUserEmail {
            @Test
            @DisplayName("UserNotFoundException을 반환한다")
            void itReturnsUserNotFoundException() {
                assertThatThrownBy(() -> userService.findUserByEmailPassword(UNKNOWN_EMAIL, PASSWORD))
                        .isInstanceOf(UserNotFoundException.class);

                verify(userRepository).findUserByEmail(UNKNOWN_EMAIL);
            }
        }
    }
}
