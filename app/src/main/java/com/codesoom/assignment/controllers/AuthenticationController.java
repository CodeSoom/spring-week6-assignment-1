package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthCommand;
import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.common.mapper.AuthMapper;
import com.codesoom.assignment.dto.AuthDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/session")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final AuthMapper authMapper;

    public AuthenticationController(AuthenticationService authenticationService, AuthMapper authMapper) {
        this.authenticationService = authenticationService;
        this.authMapper = authMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthDto.SessionInfo login(@RequestBody @Valid AuthDto.LoginParam loginParam) {
        AuthCommand.Login command = authMapper.of(loginParam);
        return new AuthDto.SessionInfo(authenticationService.login(command));
    }

}
