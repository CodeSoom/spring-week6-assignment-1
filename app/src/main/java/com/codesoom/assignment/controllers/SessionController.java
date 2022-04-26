package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.SignInResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Object signin() {
        SignInResponse signInResponse = new SignInResponse("token.token.token");
        return signInResponse;
    }
}
