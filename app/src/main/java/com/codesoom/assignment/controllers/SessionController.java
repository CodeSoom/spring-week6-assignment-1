// REST API 작성하기
// 1. CREATE -> 로그인 -> Token 발행
// 2. READ (w/ Token) -> 세션 정보 전달 -> 세션 정보가 유효한가?, 유저 정보
// 3. UPDATE (w/ Token) -> Token 재발행
// 4. DELETE -> 로그아웃 -> Token 무효화

// session은 여러개가 존재할 수 없으므로, 단수형을 사용한다.


package com.codesoom.assignment.controllers;

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
    private String accessToken;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login() {
        String accessToken = authenticationService.login();

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
