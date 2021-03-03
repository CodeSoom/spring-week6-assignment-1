package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.SessionResponseData;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.checkerframework.checker.units.qual.A;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {
    private AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login() {
        String accessToken = authenticationService.login();
        
        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
