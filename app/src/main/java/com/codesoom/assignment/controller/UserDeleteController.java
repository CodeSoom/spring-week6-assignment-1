package com.codesoom.assignment.controller;

import com.codesoom.assignment.application.UserDeleteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * 회원 삭제 요청을 처리합니다.
 */
@UserController
public class UserDeleteController {

    private final UserDeleteService service;

    public UserDeleteController(UserDeleteService service) {
        this.service = service;
    }

    /**
     * 회원 식별자를 받아 해당 회원을 삭제합니다.
     *
     * @param id 회원 식별자
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    }

}
