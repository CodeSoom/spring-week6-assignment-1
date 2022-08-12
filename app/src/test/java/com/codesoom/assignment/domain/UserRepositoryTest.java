package com.codesoom.assignment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저를 저장하고 리턴한다.")
    void save() {
        User user = User.builder()
                .email("qjawlsqjacks@naver.com")
                .name("박범진")
                .password("1234")
                .build();

        assertThat(userRepository.save(user))
                .isEqualTo(user);
    }
}
