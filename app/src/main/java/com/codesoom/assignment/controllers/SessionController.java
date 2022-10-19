package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.SessionService;
import com.codesoom.assignment.dto.LoginRequestDTO;
import com.codesoom.assignment.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/session")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        String token = sessionService.login(loginRequestDTO);
        return new TokenResponse(token);
    }
}
