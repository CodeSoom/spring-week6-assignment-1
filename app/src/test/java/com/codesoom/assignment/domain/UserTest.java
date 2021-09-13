package com.codesoom.assignment.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    private String UPDATE_NAME = "updateName";
    private String UPDATE_EMAIL = "updateEmail@xxxx.com";
    private String UPDATE_PASSWORD = "13579";

    User user;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .name("name")
                .email("email@xxxx.com")
                .password("12345")
                .build();

    }

    @Test
    @DisplayName("사용자 객체가 올바르게 생성되는지 테스트")
    void user_create_test() {

        Assertions.assertEquals("name", user.getName());
        Assertions.assertEquals("email@xxxx.com", user.getEmail());
        Assertions.assertEquals("12345", user.getPassword());

    }

    @Test
    @DisplayName("사용자 객체가 올바르게 수정되는지 테스트")
    void user_update_test() {

        user.userUpdate(UPDATE_NAME, UPDATE_EMAIL, UPDATE_PASSWORD);

        Assertions.assertEquals(UPDATE_NAME, user.getName());
        Assertions.assertEquals(UPDATE_EMAIL, user.getEmail());
        Assertions.assertEquals(UPDATE_PASSWORD, user.getPassword());

    }

}

