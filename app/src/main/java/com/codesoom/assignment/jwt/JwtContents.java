package com.codesoom.assignment.jwt;

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
}
