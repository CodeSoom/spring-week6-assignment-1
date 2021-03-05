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

import java.util.Arrays;
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

    private final String SETUP_USER_NAME = "setUpName";
    private final String EXISTED_USER_EMAIL = "setUpEmail";
    private final String SETUP_USER_PASSWORD = "setUpPassword";

    private final String CREATE_USER_NAME = "createdName";
    private final String CREATE_USER_EMAIL = "createdEmail";
    private final String CREATE_USER_PASSWORD = "createdPassword";

    private final String UPDATE_USER_NAME = "updatedName";
    private final String UPDATE_USER_PASSWORD = "updatedPassword";

    private final Long EXISTED_ID = 1L;
    private final Long CREATE_ID = 2L;
    private final Long DELETED_ID = 1L;
    private final Long NOT_EXISTED_ID = 100L;

    private List<User> users;
    private User setUpUser;
    private User createUser;

    private List<UserResultData> resultUsers;
    private UserResultData setUpUserResultData;
    private UserResultData createUserResultData;

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

        createUser = User.builder()
                .id(CREATE_ID)
                .name(CREATE_USER_NAME)
                .email(CREATE_USER_EMAIL)
                .password(CREATE_USER_PASSWORD)
                .build();

        users = Arrays.asList(setUpUser, createUser);

        setUpUserResultData = UserResultData.of(setUpUser);
        createUserResultData = UserResultData.of(createUser);
        resultUsers = Arrays.asList(setUpUserResultData, createUserResultData);
    }


    @Nested
    @DisplayName("getUsers 메서드는")
    class Describe_getUsers {
        @Nested
        @DisplayName("만약 사용자 목록이 존재한다면")
        class Context_ExistsListOfUsers {
            @Test
            @DisplayName("저장되어 있는 사용자 목록을 리턴한다")
            void itReturnListOfUsers() {
                given(userRepository.findAll()).willReturn(users);

                List<UserResultData> list = userService.getUsers();
                assertThat(list).containsExactly(setUpUserResultData, createUserResultData);

                verify(userRepository).findAll();
            }
        }

        @Nested
        @DisplayName("만약 사용자 목록이 존재하지 않는다면")
        class Context_NotExistsListsOfUsers {
            @Test
            @DisplayName("비어있는 사용자 목록을 리턴한다")
            void itReturnsEmptyListOfUsers() {
                given(userRepository.findAll()).willReturn(List.of());

                List<UserResultData> list = userService.getUsers();

                assertThat(list).isEmpty();;

                verify(userRepository).findAll();
            }
        }
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
                given(userRepository.findByIdAndDeletedIsFalse(givenExistedId)).willReturn(Optional.of(setUpUser));

                UserResultData userResultData = userService.getUser(givenExistedId);

                assertThat(userResultData.getId()).isEqualTo(setUpUser.getId());

                verify(userRepository).findByIdAndDeletedIsFalse(givenExistedId);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 사용자의 아이디가 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;

            @Test
            @DisplayName("사용자를 찾을 수 없다는 메세지를 리턴한다")
            void itReturnsUserNotFoundMessage() {
                given(userRepository.findByIdAndDeletedIsFalse(givenNotExistedId)).willReturn(Optional.empty());

                assertThatThrownBy(() -> userService.getUser(givenNotExistedId))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");

                verify(userRepository).findByIdAndDeletedIsFalse(givenNotExistedId);
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

            @BeforeEach
            void setUp() {
                userCreateData = UserCreateData.builder()
                        .name(CREATE_USER_NAME)
                        .email(CREATE_USER_EMAIL)
                        .password(CREATE_USER_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("주어진 사용자를 저장하고 저장된 사용자를 리턴한다")
            void itSavesUserAndReturnsSavedUser() {
                given(userRepository.save(any(User.class))).willReturn(createUser);

                UserResultData createdUser = userService.createUser(userCreateData);

                assertThat(createdUser.getId()).isEqualTo(createUser.getId());
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
                given(userRepository.findByIdAndDeletedIsFalse(givenExistedId)).willReturn(Optional.of(setUpUser));

                UserResultData updatedUser = userService.updateUser(givenExistedId, userUpdateData);

                assertThat(updatedUser.getId()).isEqualTo(givenExistedId);
                assertThat(updatedUser.getName()).isEqualTo(userUpdateData.getName());
                assertThat(updatedUser.getEmail()).isEqualTo(EXISTED_USER_EMAIL);
                assertThat(updatedUser.getPassword()).isEqualTo(userUpdateData.getPassword());
                
                verify(userRepository).findByIdAndDeletedIsFalse(givenExistedId);
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
                given(userRepository.findByIdAndDeletedIsFalse(givenNotExistedId)).willReturn(Optional.empty());

                assertThatThrownBy(() -> userService.updateUser(givenNotExistedId, userUpdateData))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");

                verify(userRepository).findByIdAndDeletedIsFalse(givenNotExistedId);
            }
        }

        @Nested
        @DisplayName("만약 삭제된 사용자의 아이디가 주어진다면")
        class Context_WithDeletedId {
            private final Long givenDeletedId = DELETED_ID;
            private UserUpdateData userUpdateData;

            @BeforeEach
            void setUp() {
                userUpdateData = UserUpdateData.builder()
                        .name(UPDATE_USER_NAME)
                        .password(UPDATE_USER_PASSWORD)
                        .build();

                setUpUser.delete();
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 메세지를 리턴한다")
            void itReturnsNotFoundMessage() {
                given(userRepository.findByIdAndDeletedIsFalse(givenDeletedId)).willReturn(Optional.empty());

                assertThatThrownBy(() -> userService.updateUser(givenDeletedId, userUpdateData))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");

                verify(userRepository).findByIdAndDeletedIsFalse(givenDeletedId);
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
                given(userRepository.findByIdAndDeletedIsFalse(givenExistedId)).willReturn(Optional.of(setUpUser));

                UserResultData userResultData = userService.deleteUser(givenExistedId);

                assertThat(userResultData.getId()).isEqualTo(setUpUser.getId());
                assertThat(userResultData.isDeleted()).isTrue();

                verify(userRepository).findByIdAndDeletedIsFalse(givenExistedId);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 사용자의 아이디가 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;

            @Test
            @DisplayName("사용자를 찾을 수 없다는 메세지를 리턴한다")
            void itReturnsUserNotFoundMessage() {
                given(userRepository.findByIdAndDeletedIsFalse(givenNotExistedId)).willReturn(Optional.empty());

                assertThatThrownBy(() -> userService.deleteUser(givenNotExistedId))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");

                verify(userRepository).findByIdAndDeletedIsFalse(givenNotExistedId);
            }
        }

        @Nested
        @DisplayName("만약 삭제된 사용자의 아이디가 주어진다면")
        class Context_WithDeletedId {
            private final Long givenDeletedId = DELETED_ID;

            @BeforeEach
            void setUp() {
                setUpUser.delete();
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 메세지를 리턴한다")
            void itReturnsNotFoundMessage() {
                given(userRepository.findByIdAndDeletedIsFalse(givenDeletedId))
                        .willReturn(Optional.empty());

                assertThatThrownBy(() -> userService.getUser(givenDeletedId))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");

                verify(userRepository).findByIdAndDeletedIsFalse(givenDeletedId);
            }
        }
    }
}

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
