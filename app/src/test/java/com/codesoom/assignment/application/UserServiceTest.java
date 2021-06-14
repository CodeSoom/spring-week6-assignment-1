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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Nested
@DisplayName("유저 서비스 테스트")
class UserServiceTest {
    private static final String EXISTED_EMAIL_ADDRESS = "existed@example.com";
    private static final Long DELETED_USER_ID = 200L;

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

        given(userRepository.findByIdAndDeletedIsFalse(1L))
                .willReturn(Optional.of(
                        User.builder()
                                .id(1L)
                                .email(EXISTED_EMAIL_ADDRESS)
                                .name("Tester")
                                .password("test")
                                .build()));

        given(userRepository.findByIdAndDeletedIsFalse(100L))
                .willReturn(Optional.empty());

        given(userRepository.findByIdAndDeletedIsFalse(DELETED_USER_ID))
                .willReturn(Optional.empty());
    }

    @Nested
    @DisplayName("Describe: 사용자를 신규 등록할 때")
    class RegisterNewUser {

        @Nested
        @DisplayName("Context: 모든 정보가 누락없이 입력된다면")
        class WithAllInformation {

            @Test
            @DisplayName("It: 신규 사용자를 생성합니다.")
            void registerUser() {
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
        @DisplayName("Context: 이메일이 중복된다면")
        class WithDuplicatedEmail {

            @Test
            @DisplayName("It: 이메일 중복 예외를 던집니다.")
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
    @DisplayName("Describe: 사용자 정보를 수정 할 때")
    class DescribeUpdateUserInfo {

        @Nested
        @DisplayName("Context: 등록된 사용자라면,")
        class ContextWithRegisteredUser {
            @Test
            @DisplayName("It: 입력된 정보로 사용자 정보를 수정합니다.")
            void updateUserWithExistedId() {
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
        }

        @Nested
        @DisplayName("Context: 등록되지 않은 사용자라면,")
        class ContextWithNotRegisteredUser {

            @Test
            @DisplayName("It: 예외를 던집니다.")
            void updateUserWithNotExistedId() {
                UserModificationData modificationData = UserModificationData.builder()
                        .name("TEST")
                        .password("TEST")
                        .build();

                assertThatThrownBy(() -> userService.updateUser(100L, modificationData))
                        .isInstanceOf(UserNotFoundException.class);

                verify(userRepository).findByIdAndDeletedIsFalse(100L);
            }
        }

        @Nested
        @DisplayName("Context: 삭제된 사용자라면,")
        class ContextWithDeletedUser {
            @Test
            @DisplayName("It: UserNotFound 예외를 던집니다.")
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
    }

    @Nested
    @DisplayName("Describe: 사용자를 삭제할 때")
    class DescribeDeleteUser{

        @Nested
        @DisplayName("Context: 존재하는 사용자라면,")
        class ContextWithExistedId{
            @Test
            void deleteUserWithExistedId() {
                User user = userService.deleteUser(1L);

                assertThat(user.getId()).isEqualTo(1L);
                assertThat(user.isDeleted()).isTrue();

                verify(userRepository).findByIdAndDeletedIsFalse(1L);
            }
        }

        @Nested
        @DisplayName("Context: 등록되지 않은 사용자라면,")
        class ContextWithNotExistedId{

            @Test
            @DisplayName("It: UserNotFound 예외를 던집니다.")
            void deleteUserWithNotExistedId() {
                assertThatThrownBy(() -> userService.deleteUser(100L))
                        .isInstanceOf(UserNotFoundException.class);

                verify(userRepository).findByIdAndDeletedIsFalse(100L);
            }
        }

        @Nested
        @DisplayName("Context: 이미 삭제된 사용자라면, ")
        class ContextWithDeletedId{

            @Test
            @DisplayName("It: UserNotFound 예외를 던집니다. ")
            void deleteUserWithDeletedId() {
                assertThatThrownBy(() -> userService.deleteUser(DELETED_USER_ID))
                        .isInstanceOf(UserNotFoundException.class);

                verify(userRepository).findByIdAndDeletedIsFalse(DELETED_USER_ID);
            }
        }
    }
}
