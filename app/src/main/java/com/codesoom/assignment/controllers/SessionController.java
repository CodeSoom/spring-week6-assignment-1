//REST
// 1. Create -> 로그인 => Token
// 2. Read (w/ Token) -> 세션 정보 -> 유요한가?, 내 정보
// 3. Update (w/Token) -> Token 재발급
// 4. Delete -> 로그아웃 => Token 무효화
// sessions (복수형) -> session (단수형) 사용.

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

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * user을 로그인하고 세션응답을 리턴한다.
     *
     * @return 세션 응답
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login() {
        String accessToken = authenticationService.login();

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
