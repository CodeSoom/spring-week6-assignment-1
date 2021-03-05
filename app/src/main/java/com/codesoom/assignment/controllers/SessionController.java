package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.AuthenticationCreateData;
import com.codesoom.assignment.dto.SessionResultData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** 사용자 인증에 대해 요청한다. */
@RestController
@RequestMapping("/session")
public class SessionController {
    private final AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 주어진 사용자를 인증하고 문자열 토큰을 생성하여 로그인한다.
     *
     * @param authenticationCreateData - 토큰 문자열을 만들고자 하는 사용자
     * @return - 주어진 사용자를 이용하여 생성된 토큰 문자열
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResultData login(@RequestBody AuthenticationCreateData authenticationCreateData) {
        return authenticationService.createToken(authenticationCreateData);
    }
}
