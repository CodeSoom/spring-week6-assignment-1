// REST
// 1. Create => 로그인 => Token
// 2. Read (w/Token) => 세션 정보 => 유효한가? 내 정보
// 3. Update (w/Token) => Token 재발급
// 4. Delete => 로그아웃 => Token 무효화
// sessions (복수형) => session (단수형) 사용.

package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.dto.SessionResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
@CrossOrigin
public class SessionController {
    private AuthenticationService authenticationService;

    public SessionController(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 로그인 후 JWT를 발급해서 응답합니다.
     *
     * @return JWT
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData create(@RequestBody
                                              SessionRequestData requestData) {
        String token = authenticationService.login(requestData);

        return SessionResponseData.builder()
                                  .accessToken(token)
                                  .build();
    }
}
