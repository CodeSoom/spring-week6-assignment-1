package com.codesoom.assignment.application.user;

import com.codesoom.assignment.application.user.implement.UserQueryServiceImpl;
import com.codesoom.assignment.utils.UserSampleFactory;
import com.codesoom.assignment.common.exception.UserNotFoundException;
import com.codesoom.assignment.domain.user.User;
import com.codesoom.assignment.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("UserQueryService 클래스")
class UserQueryServiceTest {

    @DataJpaTest
    class JpaTest {
        @Autowired
        UserRepository repository;
        UserQueryService service;

        public UserRepository getUserRepository() {
            return repository;
        }

        public UserQueryService getUserService() {
            if (service == null) {
                service = new UserQueryServiceImpl(repository);
            }
            return service;
        }
    }

    @Nested
    @DisplayName("getUsers 메소드는")
    class Describe_getUsers extends JpaTest {
        private final List<User> givenUser = new ArrayList<>();

        @BeforeEach
        void prepare() {
            givenUser.add(getUserRepository().save(UserSampleFactory.createUser()));
            givenUser.add(getUserRepository().save(UserSampleFactory.createUser()));
        }

        @Test
        @DisplayName("등록된 모든 회원정보를 리턴한다")
        void it_returns_all_user_info() {
            final List<User> actualUser = getUserService().getUsers();

            assertThat(actualUser).hasSize(givenUser.size());
        }
    }

    @Nested
    @DisplayName("getUser 메소드는")
    class Describe_getUser {
        @Nested
        @DisplayName("유효한 ID가 주어지면")
        class Context_with_valid_id extends JpaTest {
            private User givenUser;

            @BeforeEach
            void prepare() {
                givenUser = getUserRepository().save(UserSampleFactory.createUser());
            }

            @Test
            @DisplayName("회원정보를 리턴한다")
            void it_returns_user() {
                final User actualUser = getUserService().getUser(givenUser.getId());

                assertThat(actualUser.getId()).isEqualTo(givenUser.getId());
                assertThat(actualUser.getName()).isEqualTo(givenUser.getName());
                assertThat(actualUser.getPassword()).isEqualTo(givenUser.getPassword());
                assertThat(actualUser.getEmail()).isEqualTo(givenUser.getEmail());
            }
        }

        @Nested
        @DisplayName("유효하지않은 ID가 주어지면")
        class Context_with_invalid_id extends JpaTest {
            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> getUserService().getUser(-1L)).isInstanceOf(UserNotFoundException.class);
            }
        }
    }
}
