package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping("/error")
    @ResponseBody
    public void handlerError(HttpServletRequest request){

        throw new InvalidTokenException("");

    }
}
