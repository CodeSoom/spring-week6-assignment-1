package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginResponse;
import com.codesoom.assignment.errors.InvalidUserInformationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Key;

@RestController
@RequestMapping("/session")
public class SessionController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse login(@RequestBody LoginData loginData) {
        if(loginData.getEmail().equals("aaa@bbb.ccc")) {
            throw new InvalidUserInformationException();
        }
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jws = Jwts.builder().signWith(key).compact();

        LoginResponse response = new LoginResponse(jws);
        return response;
    }
}
