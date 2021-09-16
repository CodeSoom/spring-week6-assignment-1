package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;

import com.codesoom.assignment.dto.LoginInfoData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
// REST
// 1. Create -> 로그인 => Token
// 2. Read -> 세션 정보 => 유효한가?, 내 정보
// 3. Update (w/Token) -> 토큰 재발급
// 4. Delete -> 로그아웃 => 토큰 무효화


@CrossOrigin
@RestController
@RequestMapping("/session")
public class SessionController {
    private final AuthenticationService authenticationService;

    public SessionController(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody @Valid final LoginInfoData loginInfoData) {
        String accessToken = authenticationService.login(loginInfoData);

        return SessionResponseData.builder()
            .accessToken(accessToken)
            .build();
    }
}
