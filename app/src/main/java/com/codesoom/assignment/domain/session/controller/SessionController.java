package com.codesoom.assignment.domain.session.controller;

import com.codesoom.assignment.domain.session.application.AuthenticationService;
import com.codesoom.assignment.domain.session.controller.dto.SessionRequestDto;
import com.codesoom.assignment.domain.session.controller.dto.SessionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
public class SessionController {
    private final AuthenticationService authenticationService;

    public SessionController(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseDto login(@RequestBody @Valid final SessionRequestDto sessionRequestDto) {
        return SessionResponseDto.builder()
                .accessToken(authenticationService.login(sessionRequestDto))
                .build();
    }
}
