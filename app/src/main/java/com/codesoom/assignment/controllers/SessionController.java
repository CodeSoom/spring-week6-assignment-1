package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.dto.UserLoginResultData;
import com.codesoom.assignment.dto.UserRegistrationData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
@CrossOrigin
public class SessionController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public UserLoginResultData login(@RequestBody @Valid UserLoginData loginData) {
        String token = userService.loginUser(
                loginData.getEmail(), loginData.getPassword()
        );

        return UserLoginResultData
                .builder()
                .accessToken(token)
                .build();
    }
}
