package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserCreateRequestDto;
import com.codesoom.assignment.dto.UserUpdateRequestDto;
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
    private UserService userService;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final Mapper dozerMapper = DozerBeanMapperBuilder.buildDefault();

    private static final Long EXIST_ID = 1L;
    private static final Long NOT_EXIST_ID = 123L;

    private static final String NAME = "양효주";
    private static final String EMAIL = "yhyojoo@codesoom.com";
    private static final String PASSWORD = "112233!!";

    private static final String DUPLICATED_EMAIL = "hyo@codesoom.com";

    private static final String UPDATE_EMAIL = "joo@codesoom.com";
    private static final String UPDATE_PASSWORD = "123!";

    private User user;
    private Long givenValidId;
    private Long givenInvalidId;

    @BeforeEach
    void setUp() {
        userService = new UserService(dozerMapper, userRepository);

        user = User.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        given(userRepository.findById(EXIST_ID))
                .willReturn(Optional.of(user));

        given(userRepository.existsByEmail(DUPLICATED_EMAIL))
                .willThrow(new UserEmailDuplicationException(
                        DUPLICATED_EMAIL));

        given(userRepository.save(any(User.class)))
                .will(invocation -> invocation.<Product>getArgument(0));
    }

    void createTest(User user) {
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
    }

    void updateTest(User user) {
        assertThat(user.getEmail()).isEqualTo(UPDATE_EMAIL);
        assertThat(user.getPassword()).isEqualTo(UPDATE_PASSWORD);
    }

    @Nested
    @DisplayName("createUser 메소드는")
    class Describe_createUser {
        UserCreateRequestDto creationData;
        User createdUser;

        @Nested
        @DisplayName("사용자 정보가 주어진다면")
        class Context_with_create_request {

            @BeforeEach
            void setUp() {
                creationData = UserCreateRequestDto.builder()
                        .name(NAME)
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("새로운 사용자를 등록한다")
            void it_returns_created_user() {
                createdUser = userService.createUser(creationData);

                verify(userRepository).save(any(User.class));

                createTest(createdUser);
            }
        }

        @Nested
        @DisplayName("중복되는 이메일이 주어진다면")
        class Context_with_duplicated_email {

            @BeforeEach
            void setUp() {
                creationData = UserCreateRequestDto.builder()
                        .name(NAME)
                        .email(DUPLICATED_EMAIL)
                        .password(PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("이미 존재하는 이메일이라는 예외를 던진다")
            void it_returns_warning_message() {
                assertThatThrownBy(() -> userService.createUser(creationData))
                        .isInstanceOf(UserEmailDuplicationException.class);

                verify(userRepository).existsByEmail(DUPLICATED_EMAIL);
            }
        }
    }

    @Nested
    @DisplayName("updateUser 메소드는")
    class Describe_updateUser {
        UserUpdateRequestDto updateRequest;
        User updatedUser;

        @Nested
        @DisplayName("등록된 사용자의 ID와 수정할 정보가 주어진다면")
        class Context_with_valid_id_and_update_request {

            @BeforeEach
            void setUp() {
                givenValidId = EXIST_ID;

                updateRequest = UserUpdateRequestDto.builder()
                        .email(UPDATE_EMAIL)
                        .password(UPDATE_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("해당 ID를 갖는 사용자의 정보를 수정한다")
            void it_returns_updated_user() {
                updatedUser = userService.updateUser(givenValidId, updateRequest);

                verify(userRepository).findById(givenValidId);

                updateTest(updatedUser);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 사용자의 ID가 주어진다면")
        class Context_with_invalid_id {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;
            }

            @Test
            @DisplayName("수정할 사용자를 찾을 수 없다는 예외를 던진다")
            void it_returns_warning_message() {
                assertThatThrownBy(() -> userService.updateUser(givenInvalidId, updateRequest))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteProduct 메소드는")
    class Describe_deleteUser {

        @Nested
        @DisplayName("등록된 사용자의 ID가 주어진다면")
        class Context_with_valid_id {

            @BeforeEach
            void setUp() {
                givenValidId = EXIST_ID;
            }

            @Test
            @DisplayName("해당 ID를 갖는 사용자를 삭제한다")
            void it_returns_deleted_user() {
                userService.deleteUser(givenValidId);

                verify(userRepository).findById(givenValidId);
                verify(userRepository).delete(any(User.class));
            }
        }

        @Nested
        @DisplayName("등록되지 않은 사용자의 ID가 주어진다면")
        class Context_without_invalid_id {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;
            }

            @Test
            @DisplayName("삭제할 사용자를 찾을 수 없다는 예외를 던진다")
            void it_returns_warning_message() {
                assertThatThrownBy(() -> userService.deleteUser(givenInvalidId))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }
}
