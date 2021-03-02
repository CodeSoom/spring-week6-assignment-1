package com.codesoom.assignment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/session")
    public void login(){
    }

}
