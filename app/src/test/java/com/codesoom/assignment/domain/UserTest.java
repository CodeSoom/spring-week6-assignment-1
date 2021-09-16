package com.codesoom.assignment.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User 클래스")
public final class UserTest {
    public static final String USER_EMAIL = "test@email.com";
    public static final String USER_NAME = "test";
    public static final String USER_PASSWORD = "test";
    public static final String INVALID_PASSWORD = USER_PASSWORD + "invalid";
    public static final User USER = User.builder()
        .email(USER_EMAIL)
        .password(USER_PASSWORD)
        .name(USER_NAME)
        .build();

    private User user;

    private User subjectInit() {
        return User.builder().build();
    }

    private void subjectChangeWith() {
        user.changeWith(USER);
    }

    private void subjectDestroy() {
        user.destroy();
    }

    private String subjectGetName() {
        return user.getName();
    }

    private String subjectGetPassword() {
        return user.getPassword();
    }

    private boolean subjectIsDeleted() {
        return user.isDeleted();
    }

    private boolean subjectAuthenticate(final String password) {
        return user.authenticate(password);
    }

    @BeforeEach
    private void beforeEach() {
        user = subjectInit();
    }

    @Nested
    @DisplayName("changeWith 메서드는")
    public final class Describe_changeWith {
        @Test
        @DisplayName("사용자의 이름과 비밀번호를 업데이트한다.")
        public void it_updates_user_name_and_password() {
            subjectChangeWith();

            assertThat(subjectGetName()).isEqualTo(USER_NAME);
            assertThat(subjectGetPassword()).isEqualTo(USER_PASSWORD);
        }
    }

    @Nested
    @DisplayName("destroy 메서드는")
    public final class Describe_destroy {
        @Test
        @DisplayName("사용자를 삭제한다.")
        public void it_deactivates_user() {
            assertThat(subjectIsDeleted()).isFalse();

            subjectDestroy();

            assertThat(subjectIsDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("authenticate 메서드는")
    public final class Describe_authenticate {
        @BeforeEach
        private void beforeEach() {
            subjectChangeWith();
        }

        @Nested
        @DisplayName("삭제된 유저인 경우")
        public final class Context_deletedUser {
            @BeforeEach
            private void beforeEach() {
                subjectDestroy();
            }

            @Test
            @DisplayName("비밀번호 일치여부와 상관없이 입력한 비밀번호가 유저의 비밀번호와 일치하지 않음을 알려준다.")
            public void it_returns_false_regardless_of_password_matches_or_not() {
                assertThat(subjectAuthenticate(INVALID_PASSWORD)).isFalse();
                assertThat(subjectAuthenticate(USER_PASSWORD)).isFalse();
            }
        }

        @Test
        @DisplayName("입력한 비밀번호가 유저의 비밀번호와 일치하는지 알려준다.")
        public void it_notifies_password_matches_or_not() {
            assertThat(subjectAuthenticate(INVALID_PASSWORD)).isFalse();
            assertThat(subjectAuthenticate(USER_PASSWORD)).isTrue();
        }
    }
}
