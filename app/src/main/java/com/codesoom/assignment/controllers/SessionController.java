package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/*
1. Create -> 로그인->토큰
2. Read
3. Update
4. Delete
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/session")
public class SessionController {

    private final AuthenticationService authenticationService;
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public SessionResponseData login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        String token = authenticationService.login(userLoginRequest);
        return SessionResponseData.builder()
                .accessToken(token)
                .build();
    }

}
