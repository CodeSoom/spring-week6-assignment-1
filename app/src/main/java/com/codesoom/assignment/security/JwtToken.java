package com.codesoom.assignment.security;


import io.jsonwebtoken.Claims;

public class JwtToken {


    public JwtToken(String privateKey) {

    }

    public Claims decode(String validToken) {
    }

    public String encode(Object userId) {
    }
}
