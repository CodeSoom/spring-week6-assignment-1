package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.ProductData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
public class SessionController {
    @Autowired
    AuthenticationService authenticationService;
    /*SessionController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }*/

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String login(@Valid User inputUser) {
        String accessToken = authenticationService.login(inputUser);
        return accessToken;
    }
}