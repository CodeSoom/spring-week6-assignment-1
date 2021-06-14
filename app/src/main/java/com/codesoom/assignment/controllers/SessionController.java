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

/**
 * 로그인 세션 관련 http 요청 처리를 담당합니다.
 */
@RestController
@RequestMapping("/session")
public class SessionController {

    private final AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 로그인 요청을 받아 엑세스 토큰을 리턴합니다.
     * @param userLoginData 로그인 하기 위한 사용자 정보
     * @return 엑세스 토큰
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
