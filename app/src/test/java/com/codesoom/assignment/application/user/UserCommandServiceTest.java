package com.codesoom.assignment.application.user;

import com.codesoom.assignment.application.user.UserCommand.Register;
import com.codesoom.assignment.application.user.implement.UserCommandServiceImpl;
import com.codesoom.assignment.utils.UserSampleFactory;
import com.codesoom.assignment.common.exception.UserNotFoundException;
import com.codesoom.assignment.common.mapper.UserMapper;
import com.codesoom.assignment.domain.user.User;
import com.codesoom.assignment.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("UserCommandService 클래스")
class UserCommandServiceTest {

    @DataJpaTest
    class JpaTest {
        @Autowired
        private UserRepository repository;
        private UserCommandService service;

        private final UserMapper userMapper = UserMapper.INSTANCE;

        public UserMapper getUserMapper() {
            return userMapper;
        }

        public UserRepository getUserRepository() {
            return repository;
        }

        public UserCommandService getUserService() {
            if (service == null) {
                service = new UserCommandServiceImpl(repository, userMapper);
            }
            return service;
        }
    }

    @Nested
    @DisplayName("createUser 메소드는")
    class Describe_createUser {
        @Nested
        @DisplayName("새로운 회원정보가 주어지면")
        class Context_with_new_user extends JpaTest {
            @Test
            @DisplayName("등록하고 회원정보를 리턴한다")
            void it_returns_registered_user() {
                final Register command = Register.builder()
                        .name("홍길동")
                        .password("test1234")
                        .email("hogn@test.com")
                        .build();

                final User savedUser = getUserService().createUser(command);

                assertThat(savedUser.getName()).isEqualTo(command.getName());
                assertThat(savedUser.getPassword()).isEqualTo(command.getPassword());
                assertThat(savedUser.getEmail()).isEqualTo(command.getEmail());
            }
        }
    }

    @Nested
    @DisplayName("updateUser 메소드는")
    class Describe_updateUser {
        @Nested
        @DisplayName("유효한 ID가 주어지면")
        class Context_with_valid_id extends JpaTest {
            private User savedUser;

            @BeforeEach
            void prepare() {
                savedUser = getUserRepository().save(UserSampleFactory.createUser());
            }

            @Test
            @DisplayName("회원정보를 수정하고 리턴한다")
            void it_returns_modified_user() {
                final UserCommand.UpdateRequest command = UserCommand.UpdateRequest.builder()
                        .id(savedUser.getId())
                        .name("임꺽정")
                        .password("test1111")
                        .email("test@gmail.com").build();

                final User updatedUser = getUserService().updateUser(command);

                assertThat(updatedUser.getId()).isEqualTo(command.getId());
                assertThat(updatedUser.getName()).isEqualTo(command.getName());
                assertThat(updatedUser.getPassword()).isEqualTo(command.getPassword());
                assertThat(updatedUser.getEmail()).isEqualTo(command.getEmail());
            }
        }

        @Nested
        @DisplayName("유효하지않은 ID가 주어지면")
        class Context_with_invalid_id extends JpaTest {
            private final Long USER_ID = -1L;
            private final UserCommand.UpdateRequest command = UserMapper.INSTANCE.of(USER_ID, UserSampleFactory.createUpdateParam());

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> getUserService().updateUser(command)).isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteUser 메소드는")
    class Describe_deleteUser {
        @Nested
        @DisplayName("유효한 ID가 주어지면")
        class Context_with_valid_id extends JpaTest {
            private User savedUser;
            @BeforeEach
            void prepare() {
                savedUser = getUserRepository().save(UserSampleFactory.createUser());
            }

            @Test
            @DisplayName("회원정보를 삭제한다")
            void it_deletes_user() {
                final int beforeCnt = getUserRepository().findAll().size();

                getUserService().deleteUser(savedUser.getId());

                final int afterCnt = getUserRepository().findAll().size();

                final Optional<User> findUser = getUserRepository().findById(savedUser.getId());

                assertThat(afterCnt).isEqualTo(beforeCnt - 1);
                assertThat(findUser).isEmpty();
            }
        }

        @Nested
        @DisplayName("유효하지않은 ID가 주어지면")
        class Context_with_invalid_id extends JpaTest {
            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> getUserService().deleteUser(-1L)).isInstanceOf(UserNotFoundException.class);
            }
        }
    }
}
