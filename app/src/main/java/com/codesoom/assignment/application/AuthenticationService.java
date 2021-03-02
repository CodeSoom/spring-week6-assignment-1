package com.codesoom.assignment.application;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

  public String encode(Long userId) {
    String secret = "qwertyuiopqwertyuiopqwertyuiopqw";
    Key key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder()
        .claim("userId", userId)
        .signWith(key)
        .compact();
  }
}
