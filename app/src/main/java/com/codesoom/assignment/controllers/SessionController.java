package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
@CrossOrigin
public class SessionController {

    private AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody UserLoginData userLoginData){

        String accessToken =  authenticationService.login(userLoginData);

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
