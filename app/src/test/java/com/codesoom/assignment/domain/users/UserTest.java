package com.codesoom.assignment.domain.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserTest {

    private static final Long ID = 1L;
    private static final String NAME = "홍길동";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    @DisplayName("기본 생성자는 회원 인스턴스를 리턴한다.")
    @Test
    void createNoArgsConstructor () {
        assertThat(new User()).isNotNull();
    }

    @DisplayName("생성자에 입력된 값들로 회원 정보를 초기화 할 수 있다.")
    @Test
    void createWithAllArgsConstructorTest() {
        final User user = new User(ID, NAME, EMAIL, PASSWORD);

        assertThat(user).isNotNull();
        assertAll(() -> {
           assertThat(user.getId()).isEqualTo(ID);
           assertThat(user.getName()).isEqualTo(NAME);
           assertThat(user.getEmail()).isEqualTo(EMAIL);
           assertThat(user.getPassword()).isEqualTo(PASSWORD);
        });
    }

}
