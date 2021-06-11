package com.codesoom.assignment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;

import static org.assertj.core.api.Assertions.assertThat;
@DisplayName("User 클래스")
class UserTest {

    private User user;

    @Test
    @DisplayName("입력된 정보를 통해 회원정보를 생성합니다.")
    void it_build_user() {
        user = User.builder()
                .email("test@test.com")
                .name("tester")
                .password("password")
                .build();

        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getName()).isEqualTo("tester");
        assertThat(user.getPassword()).isEqualTo("password");
    }

    @Test
    @DisplayName("입력된 정보를 통해 회원정보를 변경합니다.")
    void changeWith() {
        User user = User.builder().build();

        user.changeWith(User.builder()
                .name("TEST")
                .password("TEST")
                .build());

        assertThat(user.getName()).isEqualTo("TEST");
        assertThat(user.getPassword()).isEqualTo("TEST");
    }

    @Test
    @DisplayName("지정받은 회원정보를 비활성화 합니다.")
    void destroy() {
        User user = User.builder().build();

        assertThat(user.isDeleted()).isFalse();

        user.destroy();

        assertThat(user.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("로그인 시 활성화 되어 있는 회원정보의 비밀번호를 대조, 인증 여부를 리턴합니다.")
    void authenticate() {
        User user = User.builder()
                .password("test")
                .build();

        assertThat(user.authenticate("test")).isTrue();
        assertThat(user.authenticate("xxx")).isFalse();
    }

    @Test
    @DisplayName("비활성화 되어 있는 회원정보는 로그인 시 인증정보를 실패로 리턴합니다.")
    void authenticateWithDeletedUser() {
        User user = User.builder()
                .password("test")
                .deleted(true)
                .build();

        assertThat(user.authenticate("test")).isFalse();
        assertThat(user.authenticate("xxx")).isFalse();
    }
}
