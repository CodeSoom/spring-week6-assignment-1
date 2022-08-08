package com.codesoom.assignment.controllers;


//REST
//1. Create -> 로그인 -> token
//2. Read (w/token) 0> 세션정보 -> 유효한가 ? 내정보
//3. Update -> 토큰 재발급
//4. Delete -> 로그 아웃 -> token 무효화
// session(단수형) 사용

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

    private AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService){
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
