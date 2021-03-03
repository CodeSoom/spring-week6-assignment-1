package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
public class SessionController {
    private final AuthenticationService authenticationService = new AuthenticationService();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse login(@RequestBody LoginData loginData) {
        User authenticUser = authenticationService.authenticate(loginData.getEmail(), loginData.getPassword());
        String token = authenticationService.issueToken(authenticUser);
        return new LoginResponse(token);
    }
}
