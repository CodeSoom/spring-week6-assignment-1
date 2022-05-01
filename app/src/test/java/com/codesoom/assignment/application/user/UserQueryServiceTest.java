package com.codesoom.assignment.application.user;


import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.domain.UserRepositoryTestDouble;
import com.codesoom.assignment.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("UserQueryService 에서")
class UserQueryServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRepositoryTestDouble userRepositoryTestDouble;
    @Autowired
    private UserQueryService userQueryService;

    /**
     * 하나의 User 를 생성해 등록합니다
     * @return 생성된 User를 반환
     */
    private User createUser() {
        User user = User.of(
                "username1",
                "example@example.com",
                "password"
        );
        return userRepository.save(user);
    }

    @Nested
    @DisplayName("users 메소드는")
    class Describe_of_users {

        @Nested
        @DisplayName("조회할 수 있는 사용자의 데이터가 있다면")
        class Context_with_list {

            @BeforeEach
            void setUp() {
                createUser();
            }

            @Test
            @DisplayName("사용자가 있는 배열을 리턴한다")
            void it_return_list_with_user() {
                List<User> users = userQueryService.users();

                assertThat(users).isNotEmpty();
            }
        }

        @Nested
        @DisplayName("조회할 수 있는 사용자의 데이터가 없다면")
        class Context_with_empty_list {

            @BeforeEach
            void setUp() {
                userRepositoryTestDouble.deleteAll();
            }

            @Test
            @DisplayName("빈 배열을 리턴한다")
            void it_return_empty_list() {
                List<User> users = userQueryService.users();

                assertThat(users).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("user 메소드는")
    class Describe_of_user {

        @Nested
        @DisplayName("조회할 수 있는 id가 주어지면")
        class Context_with_valid_id {
            private Long userId;

            @BeforeEach
            void setUp() {
                User user = createUser();
                userId = user.getId();
            }

            @Test
            @DisplayName("id와 동일한 사용자를 리턴한다")
            void it_return_user() {
                User user = userQueryService.user(userId);

                assertThat(user).isNotNull();
            }
        }

        @Nested
        @DisplayName("조회할 수 없는 id가 주어지면")
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
                assertThatThrownBy(() -> userQueryService.user(userId))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }
}