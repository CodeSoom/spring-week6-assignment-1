package com.codesoom.assignment.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}")String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String enCode(Long userId) {
        return Jwts.builder().setSubject("Joe")
                .claim("userId", 1L)
                .signWith(key)
                .compact();
    }

//    public String login(){
//        String secret = "12345678901234567890123456789010";
//        Key key = Keys.hmacShaKeyFor(secret.getBytes());
////        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);//jjwt ->keys //Hmac +SHA256
//
//        return Jwts.builder().setSubject("Joe")
//                .claim("userId",1L)
//                .signWith(key)
//                .compact();
//    }

}
