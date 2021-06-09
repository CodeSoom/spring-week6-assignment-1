package com.codesoom.assignment.controllers;


// Create -> login => Token
// Read(w/Token) => 세션 정보 -> 유효한가?
// Update (w/Token) -> Token 재발급
// Delete -> logout -> token 무효화

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
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


        return SessionResponseData.builder().accessToken("a.b.c").build();
    }
}
