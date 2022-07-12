package com.codesoom.assignment.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Getter;

@Getter
public class JwtContents {
    // HEADER
    private String alg = "HS256";
    private String typ = "JWT";

    // PAYLOAD
    private String iss;
    private String sub;
    private String aud;

    public JwtContents(String iss, String sub, String aud) {
        this.iss = (iss == null) ? "" : iss;
        this.sub = (sub == null) ? "" : sub;
        this.aud = (aud == null) ? "" : aud;
    }

    public static JwtContents from(Jws<Claims> jws) {
        return new JwtContents(
                jws.getBody().get("iss").toString(),
                jws.getBody().get("sub").toString(),
                jws.getBody().get("aud").toString()
        );
    }
}
