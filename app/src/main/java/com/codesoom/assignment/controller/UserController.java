package com.codesoom.assignment.controller;

import com.codesoom.assignment.application.user.UserCommandService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserCommandService userCommandService;

    UserController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping
    public User create(@RequestBody @Valid UserDto userDto) {
        return userCommandService.createUser(userDto);
    }

    @PostMapping("{id}")
    public User update(
            @PathVariable Long id,
            @RequestBody @Valid UserDto userDto
    ) {
        return userCommandService.save(id, userDto);
    }
}
