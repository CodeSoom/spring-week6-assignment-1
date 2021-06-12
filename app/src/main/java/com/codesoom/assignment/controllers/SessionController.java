package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.TokenData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 토큰 발급에 대한 HTTP 요청과 응답을 처리한다.
 */
@RestController
@RequestMapping("/session")
@CrossOrigin
public class SessionController {
    private AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 사용자가 login 요청시 토큰 전달해 준다.
     * @return 토큰 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody SessionRequestData sessionRequestData){
        System.out.println(sessionRequestData);
        String accessToken = authenticationService.login(sessionRequestData);

        TokenData tokenData = TokenData.builder()
                .token(accessToken)
                .createTokenDate(LocalDateTime.now()).build();
        authenticationService.saveToken(tokenData);

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
