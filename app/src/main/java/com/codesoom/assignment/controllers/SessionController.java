package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthService;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/session")
@RestController
public class SessionController {

    private final AuthService authService;

    @PostMapping
    public LoginResult login(@RequestBody LoginData loginData) {
        return authService.login(loginData);
    }
}
