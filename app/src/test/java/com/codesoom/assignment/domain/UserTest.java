package com.codesoom.assignment.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    private static final String NAME = "양효주";
    private static final String EMAIL = "yhyojoo@codesoom.com";
    private static final String PASSWORD = "112233!!";

    private static final String UPDATE_EMAIL = "joo@codesoom.com";
    private static final String UPDATE_PASSWORD = "123!";

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    @Test
    @DisplayName("새로운 사용자를 등록한다")
    void creationWithBuilder() {
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    @DisplayName("사용자 정보를 수정하고 반환한다")
    void updateWith() {
        user.updateWith(User.builder()
                .email(UPDATE_EMAIL)
                .password(UPDATE_PASSWORD)
                .build()
        );

        assertThat(user.getEmail()).isEqualTo(UPDATE_EMAIL);
        assertThat(user.getPassword()).isEqualTo(UPDATE_PASSWORD);
    }
}
