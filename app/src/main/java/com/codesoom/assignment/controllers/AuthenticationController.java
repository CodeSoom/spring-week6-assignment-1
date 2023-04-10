package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.LoginRequestData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * 상태코드 테스트
     * 반환 값 테스트
     * 파라미터 테스트
     *
     * @return
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public String login(LoginRequestData loginRequestData) {
        return authenticationService.login(loginRequestData);
    }

}
