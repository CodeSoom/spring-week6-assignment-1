package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codesoom.assignment.dto.SessionResponseData;

/**
 * 인증과 관련된 작업을 서비스에서 요청한다.
 */
@RestController
@RequestMapping("/session")
public class SessionController {

    private AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     *  토큰 생성을 요청하고 세션 정보를 반환한다.
     * @return SessionResponseData
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login() {

        String accessToken = authenticationService.login();

        return SessionResponseData
            .builder()
            .accessToken(accessToken)
            .build();
    }

}
