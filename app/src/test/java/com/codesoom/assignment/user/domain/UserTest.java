package com.codesoom.assignment.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    @Test
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
    void destroy() {
        User user = User.builder().build();

        assertThat(user.isDeleted()).isFalse();

        user.destroy();

        assertThat(user.isDeleted()).isTrue();
    }

    @Test
    void authenticate() {
        User user = User.builder()
                .password("test")
                .build();

        assertThat(user.authenticate("test")).isTrue();
        assertThat(user.authenticate("xxx")).isFalse();
    }

    @Test
    void authenticateWithDeletedUser() {
        User user = User.builder()
                .password("test")
                .deleted(true)
                .build();

        assertThat(user.authenticate("test")).isFalse();
        assertThat(user.authenticate("xxx")).isFalse();
    }

    @DisplayName("사용자를 생성하고 입력한 값을 리턴한다.")
    @Test
    void createUser() {
        String email = "test@email.com";
        String name = "test";
        String password = "password";
        User user =  User.create(email, name, password);

        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getEmail()).isEqualTo(email);
    }
}
