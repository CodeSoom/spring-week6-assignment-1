package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 인증관련 http request handler.
 */
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(
            AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 로그인 데이터를 바탕으로 인증 토큰 생성후 반환한다.
     *
     * @param loginData 로그인 데이터
     * @return 인증토큰
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData create(@RequestBody @Valid UserLoginData loginData) {
        String accessToken = authenticationService.createToken(loginData);

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }

}
