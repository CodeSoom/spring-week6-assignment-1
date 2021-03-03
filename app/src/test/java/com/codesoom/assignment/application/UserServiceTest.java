package com.codesoom.assignment.application;

import com.codesoom.assignment.UserNotFoundException;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserCreateRequestDto;
import com.codesoom.assignment.dto.UserUpdateRequestDto;
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
    private static final Long EXIST_ID = 1L;
    private static final Long NOT_EXIST_ID = 123L;

    private static final String NAME = "양효주";
    private static final String EMAIL = "yhyojoo@codesoom.com";
    private static final String PASSWORD = "112233!!";

    private static final String UPDATE_EMAIL = "joo@codesoom.com";
    private static final String UPDATE_PASSWORD = "123!";

    private UserService userService;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final Mapper dozerMapper = DozerBeanMapperBuilder.buildDefault();

    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserService(dozerMapper, userRepository);

        user = User.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        given(userRepository.save(any(User.class)))
                .will(invocation -> invocation.<Product>getArgument(0));

        given(userRepository.findById(EXIST_ID))
                .willReturn(Optional.of(user));
    }

    void updateTest(UserUpdateRequestDto update) {
        assertThat(update.getEmail()).isEqualTo(UPDATE_EMAIL);
        assertThat(update.getPassword()).isEqualTo(UPDATE_PASSWORD);
    }

    @Nested
    @DisplayName("createUser 메소드는")
    class Describe_createUser {
        UserCreateRequestDto creationData;

        @Test
        @DisplayName("새로운 사용자를 등록한다")
        void it_returns_user() {
            creationData = new UserCreateRequestDto();

            userService.createUser(creationData);

            verify(userRepository).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("updateUser 메소드는")
    class Describe_updateProduct {
        UserUpdateRequestDto updateRequest;
        User updatedUser;

        @Nested
        @DisplayName("등록된 사용자의 ID와 수정할 정보가 주어진다면")
        class Context_with_valid_id_and_product {

            @BeforeEach
            void setUp() {
                updateRequest = UserUpdateRequestDto.builder()
                        .email(UPDATE_EMAIL)
                        .password(UPDATE_PASSWORD)
                        .build();

                userService.updateUser(EXIST_ID, updateRequest);

                given(userRepository.findById(EXIST_ID))
                        .willReturn(Optional.of(user));
            }

            @Test
            @DisplayName("해당 ID를 갖는 사용자의 정보를 수정한다")
            void it_returns_updated_product() {
                verify(userRepository).findById(EXIST_ID);

                updateTest(updateRequest);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 사용자의 ID가 주어진다면")
        class Context_with_invalid_id {

            @Test
            @DisplayName("수정할 사용자를 찾을 수 없다는 예외를 던진다")
            void it_returns_warning_message() {
                assertThatThrownBy(() -> userService.updateUser(NOT_EXIST_ID, updateRequest))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteProduct 메소드는")
    class Describe_deleteProduct {
        User user;

        @Nested
        @DisplayName("등록된 사용자의 ID가 주어진다면")
        class Context_with_valid_id {

            @Test
            @DisplayName("해당 ID를 갖는 사용자를 삭제한다")
            void it_returns_deleted_user() {
                user = new User();

                userService.deleteUser(EXIST_ID);

                verify(userRepository).findById(EXIST_ID);
                verify(userRepository).delete(any(User.class));
            }
        }

        @Nested
        @DisplayName("등록되지 않은 사용자의 ID가 주어진다면")
        class Context_without_invalid_id {

            @Test
            @DisplayName("삭제할 사용자를 찾을 수 없다는 예외를 던진다")
            void it_returns_warning_message() {
                assertThatThrownBy(() -> userService.deleteUser(NOT_EXIST_ID))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }
}
