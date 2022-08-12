package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.errors.UserEmailDuplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    @DisplayName("register 메서드는 중복되지 않은 유저 정보가 주어지면 유저를 생성하고 유저 결과 정보를 리턴한다")
    void returnRegisteredUserWhenRegisterGivenUserData() {
        // Given
        given(userRepository.existsByEmail(EMAIL)).willReturn(false);
        User user = REGISTRATION_DATA.toUser();
        given(userRepository.save(user)).willReturn(user);

        // When
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
}
