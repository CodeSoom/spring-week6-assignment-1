package com.codesoom.assignment.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {


    /**
     * 상태코드 테스트
     * 반환 값 테스트
     * 파라미터 테스트
     *
     * @return
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public String login() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return Jwts.builder().setSubject("Joe").signWith(key).compact();
    }

}
