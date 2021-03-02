package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
public class SessionController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse login() {
        LoginResponse response = new LoginResponse("las.");
        return response;
    }
}
