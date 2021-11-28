package com.codesoom.assignment.user.controller;

import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
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
 * User에 대한 HTTP 요청 처리를 담당한다.
 */
@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 요청된 User를 추가하고 추가된 것을 반환한다
     * @param user 추가 요청된 user
     * @return 추가된 user
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * 요청된 User를 수정한다.
     * @param id 수정 요청 User 식별자
     * @param user 수정 User 정보
     * @return 수정된 User
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    /**
     * 요청된 식별자와 일치하는 User를 삭제한다.
     * @param id 삭제 요청 User 식별자
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
