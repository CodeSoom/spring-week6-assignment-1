package com.codesoom.assignment.auth;

import io.jsonwebtoken.Claims;

public interface ClaimTokenAuth <T>{
    String encode(Long id);
    T decode(String token);
}
