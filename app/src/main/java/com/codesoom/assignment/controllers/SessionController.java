// REST
// 1. Create -> 로그인 -> token
// 2. Read (w/ token) -> 세션 정보 -> 유효한가?, 나의 정보
// 3. Update (w/ token) -> token 재발급
// 4. Delete -> 로그아웃 -> token 무효화

package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
@CrossOrigin
public class SessionController {

    private AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody UserLoginData loginData) {

        String accessToken = authenticationService.login(loginData);

        return SessionResponseData.builder()
                .accessToken(accessToken).build();

    }

}
