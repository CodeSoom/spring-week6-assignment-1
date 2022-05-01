package com.codesoom.assignment.domain;

import com.codesoom.assignment.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("UserRepository 에서")
class UserRepositoryTest {
    private final static String USERNAME = "username1";
    private final static String EMAIL = "example@example.com";
    private final static String PASSWORD = "password";

    @Autowired
    private UserRepository userRepository;

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
    @DisplayName("findAll 메소드는")
    class Describe_of_findAll {

        @Nested
        @DisplayName("찾을 수 있는 User가 있을 경우")
        class Context_with_users {

            @BeforeEach
            void setUp() {
                createUser();
            }

            @Test
            @DisplayName("User를 포함한 배열을 리턴한다")
            void it_returns_users() {
                List<User> users = userRepository.findAll();

                assertThat(users).isNotEmpty();
            }
        }
    }

    @Nested
    @DisplayName("findById 메소드는")
    class Describe_of_findById {

        @Nested
        @DisplayName("찾을 수 있는 객체의 Id 가 주어지면")
        class Context_with_valid_id {
            private Long userId;

            @BeforeEach
            void setUp() {
                User user = createUser();
                userId = user.getId();
            }

            @Test
            @DisplayName("Id와 동일한 객체를 리턴한다")
            void it_return_user() {
                User found = userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(userId));

                assertThat(found).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("save 메소드는")
    class Describe_of_save {

        @Nested
        @DisplayName("사용자를 생성할 객체가 주어지면")
        class Context_with_valid_user {
            private final User user = User.of(
                    USERNAME,
                    EMAIL,
                    PASSWORD
            );

            @Test
            @DisplayName("사용자를 생성하고, 생성한 사용자를 리턴한다")
            void it_return_user() {
                User saved = userRepository.save(user);

                assertThat(saved).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("deleteById 메소드는")
    class Describe_of_deleteById {
        private final User user = User.of(
                USERNAME,
                EMAIL,
                PASSWORD
        );

        @Nested
        @DisplayName("삭제할 수 있는 id가 주어지면")
        class Context_with_valid_id {
            private Long userId;

            @BeforeEach
            void setUp() {
                User created = userRepository.save(user);
                userId = created.getId();
            }

            @Test
            @DisplayName("사용자를 삭제한다")
            void it_delete_user() {
                userRepository.deleteById(userId);

                Optional<User> found = userRepository.findById(userId);

                assertThat(found).isEmpty();
            }
        }
    }
}
