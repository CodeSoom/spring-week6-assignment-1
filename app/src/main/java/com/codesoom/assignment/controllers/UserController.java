package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.user.UserCommand;
import com.codesoom.assignment.application.user.UserCommandService;
import com.codesoom.assignment.common.mapper.UserMapper;
import com.codesoom.assignment.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    private final UserCommandService userService;
    private final UserMapper userMapper;

    public UserController(UserCommandService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto.UserInfo registerUser(@RequestBody @Valid UserDto.RequestParam request) {
        final UserCommand.Register command = userMapper.of(request);
        return new UserDto.UserInfo(userService.createUser(command));
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto.UserInfo updateUser(@PathVariable Long id, @RequestBody @Valid UserDto.UpdateParam request) {
        final UserCommand.UpdateRequest command = userMapper.of(id, request);
        return new UserDto.UserInfo(userService.updateUser(command));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
