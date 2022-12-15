package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
public class SessionController {

    private final AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 이메일과 비밀번호를 입력받고 존재하는 회원일 경우 JWT를 생성받아 리턴해준다.
     * @param userLoginData - email, password
     * @return jwt
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody @Valid UserLoginData userLoginData) {
        String accessToken = authenticationService.login(userLoginData);

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
