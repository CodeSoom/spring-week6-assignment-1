package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserCreateData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.dto.UserUpdateData;
import com.codesoom.assignment.errors.UserEmailDuplicatedException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("UserService 테스트")
class UserServiceTest {
    private Mapper mapper;
    private UserService userService;
    private UserRepository userRepository;

    private final Long EXISTED_ID = 1L;
    private final Long NOT_EXISTED_ID = 2L;

    private final String SETUP_USER_NAME = "setUpName";
    private final String EXISTED_USER_EMAIL = "setUpEmail";
    private final String SETUP_USER_PASSWORD = "setUpPassword";

    private final String CREATE_USER_NAME = "createdName";
    private final String CREATE_USER_EMAIL = "createdEmail";
    private final String CREATE_USER_PASSWORD = "createdPassword";

    private final String UPDATE_USER_NAME = "updatedName";
    private final String UPDATE_USER_PASSWORD = "updatedPassword";

    private List<User> users;
    private User setUpUser;

    @BeforeEach
    void setUp() {
        mapper = DozerBeanMapperBuilder.buildDefault();
        userRepository = mock(UserRepository.class);
        userService = new UserService(mapper, userRepository);

        setUpUser = User.builder()
                .id(EXISTED_ID)
                .name(SETUP_USER_NAME)
                .email(EXISTED_USER_EMAIL)
                .password(SETUP_USER_PASSWORD)
                .build();

        users = List.of(setUpUser);
    }

    @Nested
    @DisplayName("getUser 메서드는")
    class Describe_getUser {
        @Nested
        @DisplayName("만약 저장되어 있는 사용자의 아이디가 주어진다면")
        class Context_WithExistedId {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("주어진 아이디에 해당하는 사용자를 리턴한다")
            void itReturnsUser() {
                given(userRepository.findById(givenExistedId)).willReturn(Optional.of(setUpUser));

                User user = userService.getUser(givenExistedId);

                assertThat(user.getId()).isEqualTo(setUpUser.getId());

                verify(userRepository).findById(givenExistedId);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 사용자의 아이디가 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;

            @Test
            @DisplayName("사용자를 찾을 수 없다는 메세지를 리턴한다")
            void itReturnsUserNotFoundMessage() {
                given(userRepository.findById(givenNotExistedId)).willReturn(Optional.empty());

                assertThatThrownBy(() -> userService.getUser(givenNotExistedId))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");

                verify(userRepository).findById(givenNotExistedId);
            }
        }
    }

    @Nested
    @DisplayName("createUser 메서드는")
    class Describe_createUser {
        @Nested
        @DisplayName("만약 사용자가 주어진다면")
        class Context_WithUser {
            private UserCreateData userCreateData;
            private User savedUser;

            @BeforeEach
            void setUp() {
                userCreateData = UserCreateData.builder()
                        .name(CREATE_USER_NAME)
                        .email(CREATE_USER_EMAIL)
                        .password(CREATE_USER_PASSWORD)
                        .build();

                savedUser = mapper.map(userCreateData, User.class);
            }

            @Test
            @DisplayName("주어진 사용자를 저장하고 저장된 사용자를 리턴한다")
            void itSavesUserAndReturnsSavedUser() {
                given(userRepository.save(any(User.class))).willReturn(savedUser);

                UserResultData createdUser = userService.createUser(userCreateData);

                assertThat(createdUser.getName()).isEqualTo(userCreateData.getName());
                assertThat(createdUser.getEmail()).isEqualTo(userCreateData.getEmail());
                assertThat(createdUser.getPassword()).isEqualTo(userCreateData.getPassword());

                verify(userRepository).save(any(User.class));
            }
        }

        @Nested
        @DisplayName("만약 이미 저장되어 있는 이메일을 가진 사용자가 주어진다면")
        class Context_WithUserWithDuplicatedEmail {
            private UserCreateData userCreateData;
            private final String givenExistedEmail = EXISTED_USER_EMAIL;

            @BeforeEach
            void setUp() {
                userCreateData = UserCreateData.builder()
                        .name(CREATE_USER_NAME)
                        .email(givenExistedEmail)
                        .password(CREATE_USER_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("이메일이 중복되었다는 메세지를 리턴한다")
            void itReturnsEmailDuplicatedMessage() {
                given(userRepository.existsByEmail(givenExistedEmail)).willReturn(true);

                assertThatThrownBy(() -> userService.createUser(userCreateData))
                        .isInstanceOf(UserEmailDuplicatedException.class);

                verify(userRepository).existsByEmail(givenExistedEmail);
            }
        }
    }

    @Nested
    @DisplayName("updateUser 메서드는")
    class Describe_updateUser {
        @Nested
        @DisplayName("만약 저장되어 있는 사용자의 아이디와 수정 할 사용자가 주어진다면")
        class Context_WithExistedIdAndUser {
            private final Long givenExistedId = EXISTED_ID;
            private UserUpdateData userUpdateData;

            @BeforeEach
            void setUp() {
                userUpdateData = UserUpdateData.builder()
                        .name(UPDATE_USER_NAME)
                        .password(UPDATE_USER_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("사용자를 수정하고 수정된 사용자를 리턴한다")
            void itUpdatesUserAndReturnsUpdatedUser() {
                given(userRepository.findById(givenExistedId)).willReturn(Optional.of(setUpUser));

                UserResultData updatedUser = userService.updateUser(givenExistedId, userUpdateData);

                assertThat(updatedUser.getId()).isEqualTo(givenExistedId);
                assertThat(updatedUser.getName()).isEqualTo(setUpUser.getName());
                assertThat(updatedUser.getEmail()).isEqualTo(EXISTED_USER_EMAIL);
                assertThat(updatedUser.getPassword()).isEqualTo(setUpUser.getPassword());
                
                verify(userRepository).findById(givenExistedId);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 사용자의 아이디가 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;
            private UserUpdateData userUpdateData;

            @BeforeEach
            void setUp() {
                userUpdateData = UserUpdateData.builder()
                        .name(UPDATE_USER_NAME)
                        .password(UPDATE_USER_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 메세지를 리턴한다")
            void itReturnsNotFoundMessage() {
                given(userRepository.findById(givenNotExistedId)).willReturn(Optional.empty());

                assertThatThrownBy(() -> userService.updateUser(givenNotExistedId, userUpdateData))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");

                verify(userRepository).findById(givenNotExistedId);
            }
        }
    }

    @Nested
    @DisplayName("deleteUser 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("만약 저장되어 있는 사용자의 아이디가 주어진다면")
        class Context_WithExistedId {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("주어진 아이디에 해당하는 사용자를 삭제하고 삭제된 사용자를 리턴한다")
            void itDeletesUserAndReturnsDeletedUser() {
                given(userRepository.findById(givenExistedId)).willReturn(Optional.of(setUpUser));

                UserResultData userResultData = userService.deleteUser(givenExistedId);

                assertThat(userResultData.getId()).isEqualTo(setUpUser.getId());
                
                verify(userRepository).findById(givenExistedId);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 사용자의 아이디가 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;

            @Test
            @DisplayName("사용자를 찾을 수 없다는 메세지를 리턴한다")
            void itReturnsUserNotFoundMessage() {
                given(userRepository.findById(givenNotExistedId)).willReturn(Optional.empty());

                assertThatThrownBy(() -> userService.deleteUser(givenNotExistedId))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");

                verify(userRepository).findById(givenNotExistedId);
            }
        }
    }
}

//package com.codesoom.assignment.application;
//
//        import com.codesoom.assignment.domain.User;
//        import com.codesoom.assignment.domain.UserRepository;
//        import com.codesoom.assignment.dto.UserModificationData;
//        import com.codesoom.assignment.dto.UserRegistrationData;
//        import com.codesoom.assignment.errors.UserEmailDuplicationException;
//        import com.codesoom.assignment.errors.UserNotFoundException;
//        import com.github.dozermapper.core.DozerBeanMapperBuilder;
//        import com.github.dozermapper.core.Mapper;
//        import org.junit.jupiter.api.BeforeEach;
//        import org.junit.jupiter.api.Test;
//
//        import java.util.Optional;
//
//        import static org.assertj.core.api.Assertions.assertThat;
//        import static org.assertj.core.api.Assertions.assertThatThrownBy;
//        import static org.mockito.ArgumentMatchers.any;
//        import static org.mockito.BDDMockito.given;
//        import static org.mockito.Mockito.mock;
//        import static org.mockito.Mockito.verify;

//class UserServiceTest {
//    private static final String EXISTED_EMAIL_ADDRESS = "existed@example.com";
//    private static final Long DELETED_USER_ID = 200L;
//
//    private UserService userService;
//
//    private final UserRepository userRepository = mock(UserRepository.class);
//
//    @BeforeEach
//    void setUp() {
//        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
//
//        userService = new UserService(mapper, userRepository);
//
//        given(userRepository.existsByEmail(EXISTED_EMAIL_ADDRESS))
//                .willReturn(true);
//
//        given(userRepository.save(any(User.class))).will(invocation -> {
//            User source = invocation.getArgument(0);
//            return User.builder()
//                    .id(13L)
//                    .email(source.getEmail())
//                    .name(source.getName())
//                    .build();
//        });
//
//        given(userRepository.findByIdAndDeletedIsFalse(1L))
//                .willReturn(Optional.of(
//                        User.builder()
//                                .id(1L)
//                                .email(EXISTED_EMAIL_ADDRESS)
//                                .name("Tester")
//                                .password("test")
//                                .build()));
//
//        given(userRepository.findByIdAndDeletedIsFalse(100L))
//                .willReturn(Optional.empty());
//
//        given(userRepository.findByIdAndDeletedIsFalse(DELETED_USER_ID))
//                .willReturn(Optional.empty());
//    }
//
//    @Test
//    void registerUser() {
//        UserRegistrationData registrationData = UserRegistrationData.builder()
//                .email("tester@example.com")
//                .name("Tester")
//                .password("test")
//                .build();
//
//        User user = userService.registerUser(registrationData);
//
//        assertThat(user.getId()).isEqualTo(13L);
//        assertThat(user.getEmail()).isEqualTo("tester@example.com");
//        assertThat(user.getName()).isEqualTo("Tester");
//
//        verify(userRepository).save(any(User.class));
//    }
//
//    @Test
//    void registerUserWithDuplicatedEmail() {
//        UserRegistrationData registrationData = UserRegistrationData.builder()
//                .email(EXISTED_EMAIL_ADDRESS)
//                .name("Tester")
//                .password("test")
//                .build();
//
//        assertThatThrownBy(() -> userService.registerUser(registrationData))
//                .isInstanceOf(UserEmailDuplicationException.class);
//
//        verify(userRepository).existsByEmail(EXISTED_EMAIL_ADDRESS);
//    }
//
//    @Test
//    void updateUserWithExistedId() {
//        UserModificationData modificationData = UserModificationData.builder()
//                .name("TEST")
//                .password("TEST")
//                .build();
//
//        User user = userService.updateUser(1L, modificationData);
//
//        assertThat(user.getId()).isEqualTo(1L);
//        assertThat(user.getEmail()).isEqualTo(EXISTED_EMAIL_ADDRESS);
//        assertThat(user.getName()).isEqualTo("TEST");
//
//        verify(userRepository).findByIdAndDeletedIsFalse(1L);
//    }
//
//    @Test
//    void updateUserWithNotExistedId() {
//        UserModificationData modificationData = UserModificationData.builder()
//                .name("TEST")
//                .password("TEST")
//                .build();
//
//        assertThatThrownBy(() -> userService.updateUser(100L, modificationData))
//                .isInstanceOf(UserNotFoundException.class);
//
//        verify(userRepository).findByIdAndDeletedIsFalse(100L);
//    }
//
//
//    @Test
//    void updateUserWithDeletedId() {
//        UserModificationData modificationData = UserModificationData.builder()
//                .name("TEST")
//                .password("TEST")
//                .build();
//
//        assertThatThrownBy(
//                () -> userService.updateUser(DELETED_USER_ID, modificationData)
//        )
//                .isInstanceOf(UserNotFoundException.class);
//
//        verify(userRepository).findByIdAndDeletedIsFalse(DELETED_USER_ID);
//    }
//
//    @Test
//    void deleteUserWithExistedId() {
//        User user = userService.deleteUser(1L);
//
//        assertThat(user.getId()).isEqualTo(1L);
//        assertThat(user.isDeleted()).isTrue();
//
//        verify(userRepository).findByIdAndDeletedIsFalse(1L);
//    }
//
//    @Test
//    void deleteUserWithNotExistedId() {
//        assertThatThrownBy(() -> userService.deleteUser(100L))
//                .isInstanceOf(UserNotFoundException.class);
//
//        verify(userRepository).findByIdAndDeletedIsFalse(100L);
//    }
//
//    @Test
//    void deleteUserWithDeletedId() {
//        assertThatThrownBy(() -> userService.deleteUser(DELETED_USER_ID))
//                .isInstanceOf(UserNotFoundException.class);
//
//        verify(userRepository).findByIdAndDeletedIsFalse(DELETED_USER_ID);
//    }
//}
