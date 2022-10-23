package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.request.LoginRequest;
import com.codesoom.assignment.dto.response.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
@CrossOrigin
public class SessionController {

    private final AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return new LoginResponse(authenticationService.login(loginRequest));
    }
}
