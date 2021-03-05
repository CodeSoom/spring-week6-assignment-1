package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserCreateRequestDto;
import com.codesoom.assignment.dto.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 사용자 관련 요청을 처리합니다.
 */
@RestController
@RequestMapping("/users")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 사용자 조회 요청을 처리하고, 해당 사용자 정보를 반환합니다.
     *
     * @param id
     * @return 해당 사용자 정보
     */
    @GetMapping("{id}")
    public User detail(@PathVariable Long id) {
        return userService.getUser(id);
    }

    /**
     * 사용자 등록 요청을 처리하고, 등록된 사용자 정보를 반환합니다.
     *
     * @param createRequest
     * @return 등록된 사용자 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody @Valid UserCreateRequestDto createRequest) {
        User user = userService.createUser(createRequest);

        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * 사용자 정보 수정 요청을 처리하고, 수정된 사용자 정보를 반환합니다.
     *
     * @param id
     * @param updateRequest
     * @return 수정된 사용자 정보
     */
    @RequestMapping(value = "{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    User update(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequestDto updateRequest
    ) {
        User user = userService.updateUser(id, updateRequest);

        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * 사용자 삭제 요청을 처리합니다.
     *
     * @param id
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
