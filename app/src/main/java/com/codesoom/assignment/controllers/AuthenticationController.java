package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.TokenResponse;
import com.codesoom.assignment.dto.UserLoginData;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/session")
public class AuthenticationController {
  private final AuthenticationService authService;

  public AuthenticationController(AuthenticationService authService) {
    this.authService = authService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  TokenResponse login(@RequestBody @Valid UserLoginData loginData) {
    String token = authService.createSession(loginData.getEmail(), loginData.getPassword());
    return TokenResponse.builder()
            .token(token)
            .build();
  }
}
