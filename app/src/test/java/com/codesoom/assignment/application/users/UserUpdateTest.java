package com.codesoom.assignment.application.users;

import com.codesoom.assignment.application.ServiceTest;
import com.codesoom.assignment.application.users.UserCommandService;
import com.codesoom.assignment.application.users.UserNotFoundException;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.domain.users.UserSaveDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserUpdateTest extends ServiceTest {

    private UserCommandService service;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setup() {
        this.service = new UserCommandService(repository);
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @DisplayName("updateUser 메서드는")
    @Nested
    class Describe_update_user {

        private final String UPDATE_NAME = "임꺽정";
        private final String UPDATE_EMAIL = "ikj@codesoom.com";
        private final String UPDATE_PASSWORD = "dlaRJrwjd777";

        @DisplayName("찾을 수 있는 회원 id가 주어지면")
        @Nested
        class Context_with_exist_user {
            private Long EXIST_USER_ID;
            private final UserSaveDto USER_TO_UPDATE
                    = new UserSaveDto(UPDATE_NAME, UPDATE_EMAIL, UPDATE_PASSWORD);

            @BeforeEach
            void setup() {
                final UserSaveDto userSaveDto
                        = new UserSaveDto("홍길동", "hkd@codesoom.com", "ghdrlfEhd898");
                this.EXIST_USER_ID = repository.save(userSaveDto.user()).getId();
            }

            @DisplayName("회원 정보를 수정 후 수정된 정보를 반환한다.")
            @Test
            void it_update_user() {
                User user = service.updateUser(EXIST_USER_ID, USER_TO_UPDATE);

                assertThat(user.getName()).isEqualTo(UPDATE_NAME);
                assertThat(user.getEmail()).isEqualTo(UPDATE_EMAIL);
                assertThat(user.getPassword()).isEqualTo(UPDATE_PASSWORD);
            }
        }

        @DisplayName("찾을 수 없는 회원 id가 주어지면")
        @Nested
        class Context_with_not_exist_user {

            private Long NOT_EXIST_USER_ID = 999L;
            private final UserSaveDto USER_TO_UPDATE
                    = new UserSaveDto(UPDATE_NAME, UPDATE_EMAIL, UPDATE_PASSWORD);

            @BeforeEach
            void setup() {
                if (repository.existsById(NOT_EXIST_USER_ID)) {
                    repository.deleteById(NOT_EXIST_USER_ID);
                }
            }

            @DisplayName("예외를 던진다.")
            @Test
            void it_update_user() {
                assertThatThrownBy(() -> service.updateUser(NOT_EXIST_USER_ID, USER_TO_UPDATE))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

}
