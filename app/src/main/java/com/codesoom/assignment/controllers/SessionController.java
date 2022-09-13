package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1. Create -> 로그인 , 토큰 반환
 * 2. Read -> 세션 조회 , 유효 확인 , 세션 정보 반환
 * 3. Update -> 토근 재발급
 * 4. Delete -> 로그아웃 , 토큰 무효화
 */
@RestController
@RequestMapping("/session")
public class SessionController {

    private final AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(){
        String accessToken = authenticationService.login();
        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
