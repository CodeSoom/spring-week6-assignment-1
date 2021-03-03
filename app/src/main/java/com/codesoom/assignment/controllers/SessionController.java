package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    private AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/session")
    public SessionResponseData login(){

        String accessToken = authenticationService.login();

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }

}
