package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.dto.SessionResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
@CrossOrigin
public class SessionController {

    private final AuthenticationService authenticationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody @Valid LoginRequestData loginRequestData) {
        String accessToken = authenticationService.login(loginRequestData);
        return SessionResponseData.builder().accessToken(accessToken).build();
    }
}
