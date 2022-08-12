package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.errors.UserEmailDuplicationException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserServiceTest {
    public static final String EMAIL = "qjawlsqjacks@naver.com";
    public static final String NAME = "BJP";
    public static final String PASSWORD = "1234";
    public static final UserRegistrationData REGISTRATION_DATA = UserRegistrationData.builder()
            .email(EMAIL)
            .name(NAME)
            .password(PASSWORD)
            .build();
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

    @Test
    @DisplayName("register 메서드는 유저 정보가 주어지면 유저를 생성하고 유저 결과 정보를 리턴한다")
    void returnRegisteredUserWhenRegisterGivenUserData() {
        // Given, When
        UserResultData resultData = userService.registerUser(REGISTRATION_DATA);

        // Then
        assertThat(resultData.getEmail()).isEqualTo(EMAIL);
        assertThat(resultData.getName()).isEqualTo(NAME);

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("register 메서드는 유저 이메일과 동일한 유저 이메일이 있으면 예외를 던진다")
    void throwExceptionWhenExistEmail() {
        // Given
        given(userRepository.existsByEmail(EMAIL)).willReturn(true);

        // When, Then
        assertThatThrownBy(() -> userService.registerUser(REGISTRATION_DATA))
                .isExactlyInstanceOf(UserEmailDuplicationException.class);

        verify(userRepository).existsByEmail(EMAIL);
    }

    @Test
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

    @Test
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

    @Test
    void updateUserWithNotExistedId() {
        UserModificationData modificationData = UserModificationData.builder()
                .name("TEST")
                .password("TEST")
                .build();

        assertThatThrownBy(() -> userService.updateUser(100L, modificationData))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByIdAndDeletedIsFalse(100L);
    }


    @Test
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

    @Test
    void deleteUserWithExistedId() {
        User user = userService.deleteUser(1L);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.isDeleted()).isTrue();

        verify(userRepository).findByIdAndDeletedIsFalse(1L);
    }

    @Test
    void deleteUserWithNotExistedId() {
        assertThatThrownBy(() -> userService.deleteUser(100L))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByIdAndDeletedIsFalse(100L);
    }

    @Test
    void deleteUserWithDeletedId() {
        assertThatThrownBy(() -> userService.deleteUser(DELETED_USER_ID))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByIdAndDeletedIsFalse(DELETED_USER_ID);
    }
}
