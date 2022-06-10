package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.JwtAuthService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.dto.LoginResponseData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtSessionController implements SessionController {
    private final JwtAuthService service;
    private final UserService userService;

    public JwtSessionController(JwtAuthService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping("/session")
    @Override
    public LoginResponseData login(@RequestBody LoginRequestData requestData) {
        User user = userService.findBy(requestData.getEmail());
        String token = service.login(user);
        return new LoginResponseData(token);
    }
}
