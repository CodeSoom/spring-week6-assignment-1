package com.codesoom.assignment.auth.controller;

import com.codesoom.assignment.auth.application.AuthenticationService;
import com.codesoom.assignment.auth.dto.LoginRequest;
import com.codesoom.assignment.auth.dto.SessionResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class SessionController {
    private final AuthenticationService authenticationService;

    /**
     * 로그인 처리를 합니다.
     *
     * @param requestDto 로그인 요청 정보
     * @return 로그인 응답 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@Valid @RequestBody LoginRequest requestDto) {
        String token = authenticationService.authenticate(requestDto);
        return new SessionResponseData(token);
    }
}
