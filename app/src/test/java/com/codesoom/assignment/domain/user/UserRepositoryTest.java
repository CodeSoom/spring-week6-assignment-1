package com.codesoom.assignment.domain.user;

import com.codesoom.assignment.utils.UserSampleFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("UserRepository 인터페이스")
class UserRepositoryTest {

    @DataJpaTest
    class JpaTest {
        @Autowired
        UserRepository userRepository;

        public UserRepository getUserRepository() {
            return userRepository;
        }
    }

    @Nested
    @DisplayName("findAll 메소드는")
    class Describe_findAll extends JpaTest {
        private final List<User> givenUsers = new ArrayList<>();

        @BeforeEach
        void prepare() {
            givenUsers.add(getUserRepository().save(UserSampleFactory.createUser(1L)));
            givenUsers.add(getUserRepository().save(UserSampleFactory.createUser(2L)));
        }

        @Test
        @DisplayName("모든 회원을 리턴한다")
        void it_returns_all_data() {
            List<User> actualUsers = getUserRepository().findAll();

            assertThat(actualUsers).hasSize(givenUsers.size());
        }
    }

    @Nested
    @DisplayName("findById 메소드는")
    class Describe_findById {
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
            void it_returns_user_info() {
                Optional<User> actualUser = getUserRepository().findById(givenUser.getId());

                assertThat(actualUser).isNotEmpty();
                assertThat(actualUser.get().getName()).isEqualTo(givenUser.getName());
                assertThat(actualUser.get().getEmail()).isEqualTo(givenUser.getEmail());
                assertThat(actualUser.get().getPassword()).isEqualTo(givenUser.getPassword());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 ID가 주어지면")
        class Context_with_non_existed_id extends JpaTest {
            @Test
            @DisplayName("'결과 없음'을 리턴한다")
            void it_returns_optional_empty() {
                assertThat(getUserRepository().findById(-1L)).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("save 메소드는")
    class Describe_save {
        @Nested
        @DisplayName("새로운 회원정보가 주어지면")
        class Context_with_new_user_info extends JpaTest {
            private final User givenUser = UserSampleFactory.createUser();

            @Test
            @DisplayName("등록하고 리턴한다")
            void it_returns_registered_user() {
                User actualUser = getUserRepository().save(givenUser);

                assertThat(actualUser.getId()).isNotNull();
                assertThat(actualUser.getName()).isEqualTo(givenUser.getName());
                assertThat(actualUser.getPassword()).isEqualTo(givenUser.getPassword());
                assertThat(actualUser.getEmail()).isEqualTo(givenUser.getEmail());

            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {
        @Nested
        @DisplayName("회원정보가 주어지면")
        class Context_with_user_info extends JpaTest {
            private User givenUser;

            @BeforeEach
            void prepare() {
                givenUser = getUserRepository().save(UserSampleFactory.createUser());
            }

            @Test
            @DisplayName("회원정보를 삭제한다")
            void it_deletes_user_info() {
                getUserRepository().delete(givenUser);

                Optional<User> actual = getUserRepository().findById(givenUser.getId());

                assertThat(actual).isEmpty();
            }
        }
    }
}
