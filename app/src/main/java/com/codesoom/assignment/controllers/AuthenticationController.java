package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public String login(@PathVariable("id") Long id) {
        return service.create(id);
    }
}
