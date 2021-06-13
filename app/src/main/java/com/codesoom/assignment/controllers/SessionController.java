package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("session")
public class SessionController {

    private final AuthenticationService authenticationService;

    /**
     * 로그인 후 JWT를 발급해서 응답합니다.
     *
     * @return JWT
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData create(@RequestBody SessionRequestData requestData) {
        String token = authenticationService.login(requestData);

        return SessionResponseData.builder()
                .accessToken(token)
                .build();
    }
}
