package com.codesoom.assignment.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Getter;

/**
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7519">RFC7519 - JSON Web Token (JWT)</a>
 */
@Getter
public class JwtContents {
    // HEADER
    private String alg = "HS256"; // algorithm, hash algorithm used to sign the JWT
    private String typ = "JWT"; // type, media type of JWT

    // PAYLOAD
    private String iss; // issuer, identifies the principal that issued the JWT
    private String sub; // subject, identifies the principal that is the subject of the JWT
    private String aud; // audience, identifies the recipients that the JWT is intended for

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
