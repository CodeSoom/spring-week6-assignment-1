package com.codesoom.assignment.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User 클래스")
class UserTest {
    private static final Long SETUP_ID = 1L;
    private static final String SETUP_NAME = "setupName";
    private static final String SETUP_EMAIL = "setupEmail";
    private static final String SETUP_PASSWORD = "setupPassword";

    private static final String NOT_EXISTED_PASSWORD = "notExistedPassword";

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(SETUP_ID)
                .name(SETUP_NAME)
                .email(SETUP_EMAIL)
                .password(SETUP_PASSWORD)
                .build();
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        @Test
        @DisplayName("만약 사용자를 삭제한다면 삭제 여부가 true 로 바뀐다")
        void delete() {
            assertThat(user.isDeleted()).isFalse();

            user.delete();

            assertThat(user.isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("authenticate 메서드는")
    class Describe_authenticate {
        @Nested
        @DisplayName("만약 사용자가 삭제 되지 않았고 저장되어 있는 비밀번호가 주어진다면")
        class Context_UserNotDeletedAndWithExistedPassword {
            private final String givenExistedPassword = SETUP_PASSWORD;

            @Test
            @DisplayName("true 를 리턴한다")
            void itReturnsTrue() {
                assertThat(user.isDeleted()).isFalse();
                assertThat(user.authenticate(givenExistedPassword)).isTrue();
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 비밀번호가 주어진다면")
        class Context_WithNotExistedPassword {
            private final String givenNotExistedPassword = NOT_EXISTED_PASSWORD;

            @Test
            @DisplayName("false 를 리턴한다")
            void itReturnsTrue() {
                assertThat(user.isDeleted()).isFalse();
                assertThat(user.authenticate(givenNotExistedPassword)).isFalse();
            }
        }

        @Nested
        @DisplayName("만약 사용자가 삭제되었다면")
        class Context_UserDeleted {
            private User deletedUser;
            private String givenPassword = SETUP_PASSWORD;

            @BeforeEach
            void setUp() {
                deletedUser = User.builder()
                        .id(SETUP_ID)
                        .name(SETUP_NAME)
                        .email(SETUP_EMAIL)
                        .password(SETUP_PASSWORD)
                        .deleted(true)
                        .build();
            }

            @Test
            @DisplayName("false 를 리턴한다")
            void itReturnsFalse() {
                assertThat(deletedUser.isDeleted()).isTrue();
                assertThat(deletedUser.authenticate(givenPassword)).isFalse();
            }
        }
    }

//    @Test
//    void authenticate() {
//        User user = User.builder()
//                .password("test")
//                .build();
//
//        assertThat(user.authenticate("test")).isTrue();
//        assertThat(user.authenticate("xxx")).isFalse();
//    }

//    @Test
//    void authenticateWithDeletedUser() {
//        User user = User.builder()
//                .password("test")
//                .deleted(true)
//                .build();
//
//        assertThat(user.authenticate("test")).isFalse();
//        assertThat(user.authenticate("xxx")).isFalse();
//    }
}
