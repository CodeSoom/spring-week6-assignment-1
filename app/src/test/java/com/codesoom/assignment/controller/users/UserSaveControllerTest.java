package com.codesoom.assignment.controller.users;

import com.codesoom.assignment.application.users.UserSaveService;
import com.codesoom.assignment.controller.users.UserSaveController;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.domain.users.UserResponseDto;
import com.codesoom.assignment.domain.users.UserSaveDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserSaveController 클래스")
@ActiveProfiles("test")
@SpringBootTest
public class UserSaveControllerTest {

    private UserSaveController controller;

    @Autowired
    private UserSaveService service;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setup() {
        this.controller = new UserSaveController(service);
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

        final UserResponseDto userResponseDto = controller.saveUser(userSaveDto);

        assertThat(userResponseDto).isNotNull();
        assertThat(repository.findById(userResponseDto.getId())).isNotEmpty();
    }

}
