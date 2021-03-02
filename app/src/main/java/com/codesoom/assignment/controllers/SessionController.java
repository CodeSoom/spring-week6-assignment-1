package com.codesoom.assignment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
public class SessionController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    String login() {
        return "las.";
    }
}
