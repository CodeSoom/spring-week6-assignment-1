package com.codesoom.assignment.session.controller;

import com.codesoom.assignment.session.dto.SessionResponseData;
import com.codesoom.assignment.session.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {
    private final AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@PathVariable Long id) {
        String accessToken = authenticationService.login(id);
        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
