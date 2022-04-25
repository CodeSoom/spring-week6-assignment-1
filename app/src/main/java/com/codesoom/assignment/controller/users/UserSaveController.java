package com.codesoom.assignment.controller.users;

import com.codesoom.assignment.application.users.UserSaveService;
import com.codesoom.assignment.domain.users.UserResponseDto;
import com.codesoom.assignment.domain.users.UserSaveDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

/**
 * 회원 생성 요청을 처리합니다.
 */
@UserController
public class UserSaveController {

    private final UserSaveService service;

    public UserSaveController(UserSaveService service) {
        this.service = service;
    }

    /**
     * 회원 정보를 받아 생성하고, 생성 정보를 반환합니다.
     *
     * @param userSaveDto 회원 생성 정보
     * @return 등록된 회원
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponseDto saveUser(@Valid @RequestBody UserSaveDto userSaveDto) {
        return new UserResponseDto(service.saveUser(userSaveDto));
    }

}
