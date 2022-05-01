package com.codesoom.assignment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    private static final String USERNAME = "사용자이름";
    private static final String EMAIL = "example@example.com";
    private static final String PASSWORD = "password";

    @Test
    @DisplayName("of() 메소드로 User 생성할 때")
    void creationWithOf() {
        User user = User.of(
                USERNAME,
                EMAIL,
                PASSWORD
        );
        assertThat(user.getUsername()).isEqualTo(USERNAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
    }
}
