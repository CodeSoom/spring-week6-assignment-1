package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidUserEmailOrPasswordException;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
public class SessionController {
    private AuthenticationService authenticationService;
    private UserService userService;

    public SessionController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(
            @RequestBody @Valid UserLoginData userLoginData) {
        User user = this.userService.findUserByEmail(userLoginData.getEmail());
        if(user == null){
            throw new InvalidUserEmailOrPasswordException();
        }

        if( user.getPassword().equals(userLoginData.getPassword()) ){
            String accessToken = authenticationService.login(user.getId());
            return SessionResponseData.builder().accessToken(accessToken).build();
        }else{
            throw new InvalidUserEmailOrPasswordException();
        }
    }

    @ExceptionHandler(InvalidUserEmailOrPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleMissingRequestHeaderException(){

    }
}
