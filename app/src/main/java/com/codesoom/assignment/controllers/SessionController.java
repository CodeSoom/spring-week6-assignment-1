package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.LoginForm;
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
 * token 에 대한 HTTP 요청을 처리한다.
 * 1. 로그인 POST /session UserRegistrationData
 */
@RestController
@RequestMapping("/session")
@CrossOrigin
@RequiredArgsConstructor
public class SessionController {
    private final AuthenticationService authenticationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String login(@RequestBody @Valid LoginForm form) {
        return authenticationService.login(form);
    }


}
