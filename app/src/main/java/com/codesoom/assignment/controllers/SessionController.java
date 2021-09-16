package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {
    private AuthenticationService authenticationService;
    SessionController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String login() {
        String accessToken = authenticationService.login();
        return accessToken;
    }
}