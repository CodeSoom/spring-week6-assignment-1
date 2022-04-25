package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.domain.UserSaveDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSaveTest extends ServiceTest {

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

    @DisplayName("회원 정보를 성공적으로 저장한다.")
    @Test
    void saveUserTest() {
        final UserSaveDto userSaveDto = new UserSaveDto("홍길동", "email", "password");

        final User user = service.saveUser(userSaveDto);

        assertThat(user).isNotNull();
        assertThat(repository.findById(user.getId())).isNotEmpty();
    }

}
