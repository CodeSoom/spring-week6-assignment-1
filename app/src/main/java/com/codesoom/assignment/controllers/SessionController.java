package com.codesoom.assignment.controllers;

import com.codesoom.assignment.jwt.JsonWebToken;
import com.codesoom.assignment.jwt.JwtContents;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
@CrossOrigin
public class SessionController {
    @PostMapping
    public String generateJwt(@RequestBody JwtContents jwtContents) {
        return JsonWebToken.generate(jwtContents);
    }

}
