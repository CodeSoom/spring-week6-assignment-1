package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.TokenResponse;
import com.codesoom.assignment.dto.UserLoginData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 유저 인증에 대한 HTTP 요청 핸들러.
 */
@RestController
@RequestMapping("/session")
@CrossOrigin
public class AuthenticationController {
    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    /**
     * 주어진 유저 로그인 정보로 인증을 시도하고 토큰을 응답합니다.
     *
     * @param loginData 유저 로그인 정보
     * @return 토큰
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    TokenResponse login(@RequestBody @Valid UserLoginData loginData) {
        String token = authService.createSession(loginData);
        return TokenResponse.builder()
                .accessToken(token)
                .build();
    }
}
