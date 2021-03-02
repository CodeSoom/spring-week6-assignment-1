package com.codesoom.assignment.auth.controller;

import com.codesoom.assignment.auth.application.AuthenticationService;
import com.codesoom.assignment.auth.dto.AuthenticationRequestDto;
import com.codesoom.assignment.auth.dto.SessionResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/session")
@RestController
@RequiredArgsConstructor
public class SessionController {
    private final AuthenticationService authenticationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@Valid @RequestBody AuthenticationRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
