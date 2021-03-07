package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginResponse;
import com.codesoom.assignment.infra.InMemoryUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/session")
public class SessionController {
    private final AuthenticationService authenticationService;
    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse login(@RequestBody LoginData loginData) {
        User authenticUser = authenticationService.authenticate(loginData.getEmail(), loginData.getPassword());
        String token = authenticationService.issueToken(authenticUser);
        return new LoginResponse(token);
    }
}
