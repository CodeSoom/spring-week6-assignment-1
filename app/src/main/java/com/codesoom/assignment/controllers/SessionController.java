package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 세션과 관련된 HTTP 요청 처리를 담당합니다.
 */
@RestController
@RequestMapping("/session")
@CrossOrigin
@RequiredArgsConstructor
public class SessionController {

    private final AuthenticationService authenticationService;

    /**
     * 주어진 회원 로그인 정보에 해당하는 액세스 토큰을 응답합니다.
     *
     * @param userLoginData 회원 로그인 정보
     * @return 생성된 액세스 토큰
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody UserLoginData userLoginData) {
        String accessToken = authenticationService.login(userLoginData);

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }

}
