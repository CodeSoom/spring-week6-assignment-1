package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 세션 관련 HTTP 처리를 담당합니다.
 */
@RestController
@RequestMapping("/session")
public class SessionController {

    private final AuthenticationService authenticationService;

    public SessionController(
        AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 회원 인증 확인 후 생성한 JWT를 리턴합니다.
     *
     * @param user 회원
     * @return 생성된 JWT
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccessToken login(@RequestBody User user) {
        return authenticationService.authenticate(user);
    }
}
