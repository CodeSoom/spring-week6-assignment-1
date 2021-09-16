package com.codesoom.assignment.domain;

import com.codesoom.assignment.infra.JpaUserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.codesoom.assignment.domain.UserTest.USER;
import static com.codesoom.assignment.domain.UserTest.USER_EMAIL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@DisplayName("UserRepository 클래스")
public class UserRepositoryTest {
    public static final String INVALID_EMAIL = USER_EMAIL + "invalid";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @BeforeEach
    private void beforeEach() {
        jpaUserRepository.deleteAll();
    }

    private User subjectSave(final User user) {
        return userRepository.save(user);
    }

    private Optional<User> subjectFindByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Nested
    @DisplayName("findByEmail 메서드는")
    public final class Context_findByEmail {
        @Nested
        @DisplayName("email에 해당하는 유저가 있는 경우")
        public final class Context_validEmail {
            @BeforeEach
            private void beforeEach() {
                subjectSave(USER);
            }

            @Test
            @DisplayName("찾은 유저를 리턴한다.")
            public void it_returns_an_found_user() {
                assertThat(subjectFindByEmail(USER_EMAIL))
                    .isPresent();
            }
        }

        @Nested
        @DisplayName("email에 해당하는 유저가 없는 경우")
        public final class Context_invalidEmail {
            @Test
            @DisplayName("빈 값을 리턴한다.")
            public void it_returns_an_empty_value() {
                assertThat(subjectFindByEmail(INVALID_EMAIL))
                    .isEmpty();
            }
        }
    }
}
