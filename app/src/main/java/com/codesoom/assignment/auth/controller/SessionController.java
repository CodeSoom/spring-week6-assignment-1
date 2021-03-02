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

/**
 * 사용자 인증 요청을 처리합니다.
 */
@RequestMapping("/session")
@RestController
@RequiredArgsConstructor
public class SessionController {
    private final AuthenticationService authenticationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@Valid @RequestBody AuthenticationRequestDto requestDto) {
        String token = authenticationService.authenticate(requestDto.getEmail(), requestDto.getPassword());
        return new SessionResponseData(token);
    }
}
