package com.codesoom.assignment.application.user;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserDto;
import com.codesoom.assignment.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("UserCommandService 에서")
class UserCommandServiceTest {
    private final static String USERNAME = "username1";
    private final static String EMAIL = "example@example.com";
    private final static String PASSWORD = "password";

    private final static String UPDATE_USERNAME = "updated";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCommandService userCommandService;

    /**
     * 하나의 User 를 생성해 등록합니다
     * @return 생성된 User를 반환
     */
    private User createUser() {
        User user = User.of(
                USERNAME,
                EMAIL,
                PASSWORD
        );
        return userRepository.save(user);
    }

    @Nested
    @DisplayName("createUser 메소드는")
    class Describe_of_create_user {

        @Nested
        @DisplayName("생성할 수 있는 사용자의 데이터가 주어지면")
        class Context_with_valid_user_data {
            private final UserDto userData = UserDto.builder()
                    .username(USERNAME)
                    .email(EMAIL)
                    .password(PASSWORD)
                    .build();

            @Test
            @DisplayName("사용자를 생성한 후, 생성된 사용자를 리턴한다")
            void it_create_and_return_user() {
                User user = userCommandService.createUser(userData);

                assertThat(user).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("save 메소드는")
    class Describe_of_save {
        private final UserDto userDto = UserDto.builder()
                .username(UPDATE_USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        @Nested
        @DisplayName("업데이트할 수 있는 사용자의 id와 데이터가 주어지면")
        class Context_with_valid_id {
            private Long userId;

            @BeforeEach
            void setUp() {
                User user = createUser();
                userId = user.getId();
            }

            @Test
            @DisplayName("사용자를 수정하고, 수정된 사용자를 반환한다")
            void it_return_user() {
                User user = userCommandService.save(userId, userDto);

                assertThat(user).isNotNull();
                assertThat(user.getUsername()).isEqualTo(UPDATE_USERNAME);
            }
        }

        @Nested
        @DisplayName("업데이트할 수 없는 사용자의 id가 주어지면")
        class Context_with_invalid_id {
            private Long userId;

            @BeforeEach
            void setUp() {
                User user = createUser();
                userId = user.getId();
                userRepository.deleteById(userId);
            }

            @Test
            @DisplayName("UserNotFoundException을 던진다")
            void it_throw_userNotFoundException() {
                assertThatThrownBy(() -> userCommandService.save(userId, userDto))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteById 메소드는")
    class Describe_of_deleteById {

        @Nested
        @DisplayName("삭제할 수 있는 id가 주어지면")
        class Context_with_valid_id {
            private Long userId;

            @BeforeEach
            void setUp() {
                User user = createUser();
                userId = user.getId();
            }

            @Test
            @DisplayName("사용자를 삭제한다")
            void it_delete_user() {
                userCommandService.deleteById(userId);

                assertThatThrownBy(
                        () -> userRepository.findById(userId)
                                .orElseThrow(
                                        () -> new UserNotFoundException(userId)
                                )
                ).isInstanceOf(UserNotFoundException.class);
            }
        }
    }
}
