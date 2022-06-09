package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.dto.LoginResponseData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtSessionController implements SessionController {
    @PostMapping("/session")
    @Override
    public LoginResponseData login(@RequestBody LoginRequestData requestData) {
        return new LoginResponseData("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxIn0.aqbG22EmNECI69ctM6Jsas4SWOxalVlcgF05iujelq4");
    }
}
