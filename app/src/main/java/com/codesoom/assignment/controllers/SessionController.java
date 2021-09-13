package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
// REST
// 1. Create -> 로그인 => Token
// 2. Read -> 세션 정보 => 유효한가?, 내 정보
// 3. Update (w/Token) -> 토큰 재발급
// 4. Delete -> 로그아웃 => 토큰 무효화
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@CrossOrigin
@RestController
@RequestMapping("/session")
public class SessionController {
    private AuthenticationService authenticationService;

    public SessionController(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData create() {
        String accessToken = authenticationService.login();

        return SessionResponseData.builder()
            .accessToken(accessToken)
            .build();
    }
}
