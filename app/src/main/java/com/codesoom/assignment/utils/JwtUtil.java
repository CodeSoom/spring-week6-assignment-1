package com.codesoom.assignment.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  @Value("${jwt.secret}")
  private String secret;

  public JwtUtil(String secretKey) {
    this.secret = secretKey;
  }

  public String encode(Long userId) {
    Key key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder()
        .claim("userId", userId)
        .signWith(key)
        .compact();
  }
}
