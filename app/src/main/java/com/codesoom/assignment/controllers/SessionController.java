package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserSignInData;
import com.codesoom.assignment.utils.JWT;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/session")
@CrossOrigin
public class SessionController {
    private final AuthService authService;
    private final JWT jwt;

    public SessionController(AuthService authService, JWT jwt) {
        this.authService = authService;
        this.jwt = jwt;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String signIn(@RequestBody @Valid UserSignInData userSignInData) {
        final User user = authService.signIn(userSignInData.email(), userSignInData.password());
        final Map<String, Object> claims = new HashMap<>() {{
            put("id", user.getId());
            put("email", user.getEmail());
            put("name", user.getName());
        }};

        return jwt.encode(null, claims);
    }
}
