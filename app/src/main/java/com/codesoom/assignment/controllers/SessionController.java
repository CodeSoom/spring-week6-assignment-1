package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserSignInData;
import com.codesoom.assignment.errors.WrongUserPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
@CrossOrigin
public class SessionController {
    private final UserService userService;

    public SessionController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String signIn(@RequestBody @Valid UserSignInData userSignInData) {
        User user = userService.findUserByEmail(userSignInData.email());

        if (!user.getEmail().equals(userSignInData.password())) {
            throw new WrongUserPasswordException();
        }
        return ".";
    }
}
