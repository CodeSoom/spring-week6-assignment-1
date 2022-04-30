package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.dto.UserRegistrationData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {
    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody @Valid UserLoginData loginData) {
        return userService.loginUser(
                loginData.getEmail(), loginData.getPassword()
        );
    }
}
