package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// Create -> login => Token
// Read(w/Token) => 세션 정보 -> 유효한가?
// Update (w/Token) -> Token 재발급
// Delete -> logout -> token 무효화

/**
 * 로그인 관련 HTTP 처리를 담당합니다.
 */
@RestController
@RequestMapping("/session")
public class SessionController {

    private AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 로그인시 발급되는 JWT 로 accessToken 을 리턴 받습니다.
     * @return accessToken
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login() {
        String accessToken = authenticationService.login();
        return SessionResponseData.builder().accessToken(accessToken).build();
    }
}
