package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.JsonWebTokenResponse;
import com.codesoom.assignment.token.JsonWebTokenAttribute;
import com.codesoom.assignment.token.JsonWebTokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/session")
public class SessionController {

    private final UserService userService;

    private final JsonWebTokenManager jsonWebTokenManager;

    public SessionController(UserService userService, JsonWebTokenManager jsonWebTokenManager) {
        this.userService = userService;
        this.jsonWebTokenManager = jsonWebTokenManager;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JsonWebTokenResponse signin(@RequestBody Map<String, String> params) {

        final User foundUser = userService.
                findUserByEmailAndPassword(params.get("email"), params.get("password"));

        JsonWebTokenAttribute jwtAttribute = JsonWebTokenAttribute.builder()
                .id(foundUser.getId())
                .build();

        return new JsonWebTokenResponse(jsonWebTokenManager.createToken(jwtAttribute));
    }
}
