package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.errors.UserEmailDuplicationException;
import com.codesoom.assignment.errors.UserNotFoundException;
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

class UserServiceTest {
    private static final Long ID = 1L;
    private static final Long NON_EXISTING_ID = 1000L;
    private static final Long DELETED_USER_ID = 200L;
    private static final String NAME = "tester";
    private static final String NEW_NAME = "tester2";
    private static final String EMAIL = "tester@example.com";
    private static final String EXISTING_EMAIL_ADDRESS = "existing@example.com";
    private static final String PASSWORD = "test";

    private UserService userService;

    private final UserRepository userRepository = mock(UserRepository.class);

    private User user;
    private UserRegistrationData userRegistrationData;
    private UserModificationData userModificationData;

    @BeforeEach
    void setUp() {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();

        userService = new UserService(mapper, userRepository);

        user = User.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        userRegistrationData = UserRegistrationData.builder()
                    .name(NAME)
                    .email(EMAIL)
                    .password(PASSWORD)
                    .build();

        userModificationData = UserModificationData.builder()
                .name(NEW_NAME)
                .password(PASSWORD)
                .build();
    }

    @Nested
    @DisplayName("registerUser 메서드는")
    class Describe_registerUser_method {
        @Nested
        @DisplayName("모든 속성이 유효한 user가 주어졌을 경우")
        class Context_if_user_with_valid_attributes_given {
            @BeforeEach
            void setUp() {
                given(userRepository.save(any(User.class)))
                        .will(invocation -> {
                            User source = invocation.getArgument(0);

                            return User.builder()
                                    .id(ID)
                                    .email(source.getEmail())
                                    .name(source.getName())
                                    .build();
                        });
            }

            @Nested
            @DisplayName("user를 반환한다")
            class It_returns_user {
                @Test
                void test() {
                    User user = userService.registerUser(userRegistrationData);

                    assertThat(user.getId()).isEqualTo(ID);
                    assertThat(user.getName()).isEqualTo(NAME);
                    assertThat(user.getEmail()).isEqualTo(EMAIL);

                    verify(userRepository).save(any(User.class));
                }
            }
        }

        @Nested
        @DisplayName("이미 존재하는 email을 가진 user가 주어졌을 경우")
        class Context_if_user_with_existing_email_given {
            @BeforeEach
            void setUp() {
                userRegistrationData = UserRegistrationData.builder()
                        .name(NAME)
                        .email(EXISTING_EMAIL_ADDRESS)
                        .password(PASSWORD)
                        .build();

                given(userRepository.existsByEmail(EXISTING_EMAIL_ADDRESS))
                        .willReturn(true);
            }

            @Nested
            @DisplayName("UserEmailDuplicationException 예외를 던진다")
            class It_throws_userEmailDuplicationException {
                User user() {
                    return userService.registerUser(userRegistrationData);
                }

                @Test
                void test() {
                    assertThatThrownBy(() -> user())
                            .isInstanceOf(UserEmailDuplicationException.class);

                    verify(userRepository).existsByEmail(EXISTING_EMAIL_ADDRESS);
                }
            }
        }
    }

    @Nested
    @DisplayName("updateUser 메서드는")
    class Describe_updateUser_method {
        @Nested
        @DisplayName("존재하는 id가 주어진 경우")
        class Context_if_existing_id_given {
            @BeforeEach
            void setUp() {
                given(userRepository.findByIdAndDeletedIsFalse(ID))
                        .willReturn(Optional.of(
                                User.builder()
                                        .id(ID)
                                        .email(EMAIL)
                                        .name(NAME)
                                        .password(PASSWORD)
                                        .build()));
            }

            @Nested
            @DisplayName("id에 해당하는 user를 반환한다")
            class It_returns_user {
                User user() {
                    return userService.updateUser(ID, userModificationData);
                }

                @Test
                void test() {
                    assertThat(user().getId()).isEqualTo(ID);
                    assertThat(user().getEmail()).isEqualTo(EMAIL);
                    assertThat(user().getName()).isEqualTo(NEW_NAME);
                }
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어진 경우")
        class Context_if_non_existing_id_given {
            @BeforeEach
            void setUp() {
                given(userRepository.findById(NON_EXISTING_ID))
                        .willThrow(new UserNotFoundException(NON_EXISTING_ID));
            }

            @Nested
            @DisplayName("UserNotFoundException 예외를 던진다")
            class It_throws_userNotFoundException {
                User user() {
                    return userService.updateUser(NON_EXISTING_ID, userModificationData);
                }

                @Test
                void test() {
                    assertThatThrownBy(() -> user())
                            .isInstanceOf(UserNotFoundException.class);

                    verify(userRepository).findByIdAndDeletedIsFalse(NON_EXISTING_ID);
                }
            }
        }

        @Nested
        @DisplayName("삭제된 id가 주어진 경우")
        class Context_if_deleted_id_given {
            @BeforeEach
            void setUp() {
                given(userRepository.findById(DELETED_USER_ID))
                        .willThrow(new UserNotFoundException(NON_EXISTING_ID));
            }

            @Nested
            @DisplayName("UserNotFoundException 예외를 던진다")
            class It_throws_userNotFoundException {
                User user() {
                    return userService.updateUser(DELETED_USER_ID, userModificationData);
                }

                @Test
                void test() {
                    assertThatThrownBy(() -> user())
                            .isInstanceOf(UserNotFoundException.class);

                    verify(userRepository).findByIdAndDeletedIsFalse(DELETED_USER_ID);
                }
            }
        }
    }

    @Nested
    @DisplayName("deleteUser 메서드는")
    class Describe_deleteUser_method {
        @Nested
        @DisplayName("존재하는 id가 주어진 경우")
        class Context_if_existing_id_given {
            @BeforeEach
            void setUp() {
                given(userRepository.findByIdAndDeletedIsFalse(ID))
                        .willReturn(Optional.of(
                                User.builder()
                                        .id(ID)
                                        .email(EXISTING_EMAIL_ADDRESS)
                                        .name(NAME)
                                        .password(PASSWORD)
                                        .build()));
            }

            @Nested
            @DisplayName("id에 해당하는 user를 반환한다")
            class It_throws_user {
                User user() {
                    return userService.deleteUser(ID);
                }

                @Test
                void test() {
                    User user = user();
                    assertThat(user.getId()).isEqualTo(ID);
                    assertThat(user.isDeleted()).isTrue();

                    verify(userRepository).findByIdAndDeletedIsFalse(ID);
                }
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어진 경우")
        class Context_if_non_existing_id_given {
            @BeforeEach
            void setUp() {
                given(userRepository.findByIdAndDeletedIsFalse(NON_EXISTING_ID))
                        .willReturn(Optional.empty());
            }

            @Nested
            @DisplayName("UserNotFoundException 예외를 던진다")
            class It_throws_userNotFoundException {
                User user() {
                    return userService.deleteUser(NON_EXISTING_ID);
                }

                @Test
                void test() {
                    assertThatThrownBy(() -> user())
                            .isInstanceOf(UserNotFoundException.class);

                    verify(userRepository).findByIdAndDeletedIsFalse(NON_EXISTING_ID);
                }
            }
        }

        @Nested
        @DisplayName("삭제된 id가 주어진 경우")
        class Context_if_deleted_id_given {
            @BeforeEach
            void setUp() {
                given(userRepository.findByIdAndDeletedIsFalse(DELETED_USER_ID))
                        .willReturn(Optional.empty());
            }

            @Nested
            @DisplayName("UserNotFoundException 예외를 던진다")
            class It_throws_userNotFoundException {
                User user() {
                    return userService.deleteUser(DELETED_USER_ID);
                }

                @Test
                void test() {
                    assertThatThrownBy(() -> user())
                            .isInstanceOf(UserNotFoundException.class);

                    verify(userRepository).findByIdAndDeletedIsFalse(DELETED_USER_ID);
                }
            }
        }
    }
}
