package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class SessionController {
    public SessionController() {
    }

    @PostMapping
    public String signIn() {
        return ".";
    }
}
