package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.SessionResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("session")
public class SessionController {
    private final AuthenticationService authenticationService;

    private final UserService userService;

    public SessionController(AuthenticationService authenticationService,
                             UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody LoginData loginData) {
        User user = userService.findUserByEmailPassword(loginData);

        String accessToken = authenticationService.login(user.getId());

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
