package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserSignInData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
@CrossOrigin
public class SessionController {
    private final AuthService authService;

    public SessionController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String signIn(@RequestBody @Valid UserSignInData userSignInData) {
        User user = authService.signIn(userSignInData.email(), userSignInData.password());
        return ".";
    }
}
