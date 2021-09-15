package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.dto.SessionResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 인증관련 http request handler.
 */
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(
            AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData create(@RequestBody @Valid UserLoginData loginData) {
        String accessToken = authenticationService.createToken(loginData);

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }

}
