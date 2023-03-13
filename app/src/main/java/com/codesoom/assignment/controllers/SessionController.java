package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/*
1. Create -> 로그인->토큰
2. Read
3. Update
4. Delete
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/session")
public class SessionController {

    private final AuthenticationService authenticationService;


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public SessionResponseData login() {
        authenticationService.login();
        return SessionResponseData.builder()
                .accessToken("a.b.c")
                .build();
    }

}
