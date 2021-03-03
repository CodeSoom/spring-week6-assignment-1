package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.dto.UserCreateData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.dto.UserUpdateData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 사용자에 대한 요청을 한다.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 주어진 사용자 저장하고 해당 객체를 리턴한다.
     *
     * @param userCreateData - 새로 저장하고자 하는 사용자
     * @return 저장 된 사용자
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResultData create(@RequestBody @Valid UserCreateData userCreateData) {
        return userService.createUser(userCreateData);
    }

    /**
     * 주어진 식별자에 해당하는 사용자를 수정하고 해당 객체를 리턴한다.
     *
     * @param id - 수정하고자 하는 사용자의 식별자
     * @param userUpdateData - 수정 할 새로운 사용자
     * @return 수정 된 사용자
     */
    @PatchMapping("/{id}")
    public UserResultData update(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateData userUpdateData) {
        return userService.updateUser(id, userUpdateData);
    }

    /**
     * 주어진 식별자에 해당하는 사용자를 삭제하고 해당 객체를 리턴한다.
     *
     * @param id - 삭제하고자 하는 사용자의 식별자
     * @return 삭제 된 사용자
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserResultData delete(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
