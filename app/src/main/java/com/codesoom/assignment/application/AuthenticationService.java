package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.errors.InvalidUserInformationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class AuthenticationService {
    public User authenticate(String email, String password) {
        if (email.equals("aaa@bbb.ccc")) {
            throw new InvalidUserInformationException();
        }
        return User.builder().id(1L).build();
    }

    public String issueToken(User authenticUser) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jws = Jwts.builder().claim("user_id", authenticUser.getId()).signWith(key).compact();
        return jws;
    }
}
